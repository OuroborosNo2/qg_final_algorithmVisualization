package com.qgstudio.controller;

import com.qgstudio.constant.ResultEnum;
import com.qgstudio.po.CarSpeed;
import com.qgstudio.service.CarSpeedService;
import com.qgstudio.util.NumberUtil;
import com.qgstudio.vo.CarSpeedVo;
import com.qgstudio.vo.Result;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author OuroborosNo2
 * @since 2022-10-3
 */
@RestController
@RequestMapping(value = "/api/carSpeed", produces = "application/json;charset=UTF-8")
public class CarSpeedController {
    @Autowired
    private CarSpeedService carSpeedService;

    /**
     * 取出全部小车数据
     * @param tableName 所查的表
     * @return
     */
    @GetMapping("/all")
    public Result getAll(@RequestParam String tableName, @RequestParam(required = false) boolean polishId, @RequestParam(required = false)Integer pieces) throws ExecutionException, InterruptedException {
        //多线程查询
        int size = carSpeedService.getDivisionList(tableName).size();
        List<Integer> numbers = new ArrayList<>();
        for(int i=1;i<=size;i++){
            numbers.add(i);
        }
        List<List<CarSpeed>> lists = multiSelect(numbers,pieces,tableName);
        //原来不分片
        //List<List<CarSpeed>> lists = carSpeedService.get(tableName,null);
        if (lists.isEmpty()){
            return new Result(ResultEnum.ARGS_ERR);
        }
        List<CarSpeedVo> result = new ArrayList<>();
        int timeOrder = 0;
        for (List<CarSpeed> list : lists) {
            for (CarSpeed carSpeed:list) {
                carSpeed.setTimeOrder(timeOrder);
                if(polishId){
                    carSpeed.setId(carSpeed.getId()-timeOrder*(list.size()+1));
                }
            }
            result.add(new CarSpeedVo(timeOrder++,list));
        }
        return new Result(result);
    }

    /**
     * 通过抽帧算法,从所有小车数据中平均获取指定时间段数量的小车数据
     * @param tableName 所查的表
     * @param amount 指定时间段数量,若超出已有数据,则返回全部时间段数据
     * @return
     */
    @GetMapping
    public Result get(@RequestParam String tableName,@RequestParam int amount,@RequestParam(required = false) boolean polishId,@RequestParam(required = false)Integer pieces) throws ExecutionException, InterruptedException {
        List<CarSpeed> divisionList = carSpeedService.getDivisionList(tableName);
        if (divisionList.isEmpty()){
            return new Result(ResultEnum.ARGS_ERR);
        }
        List<CarSpeedVo> result = new ArrayList<>();


        //总数
        int total = divisionList.size();
        //如果要求的时间段大于已有数据,直接返回全部时间段
        if(total<amount){
            amount = total;
        }
        //抽帧算法
        List<Integer> numbers = NumberUtil.frameExtracting(total, amount);

        //多线程查询
        List<List<CarSpeed>> lists = multiSelect(numbers,pieces,tableName);

        //原来不分片
        //List<List<CarSpeed>> lists = carSpeedService.get(tableName,numbers);

        //处理
        int count = 0;
        for (int i=0;i<numbers.size();i++) {
            int timeOrder = numbers.get(i);
            //下标从0开始
            timeOrder--;
            List<CarSpeed> list = lists.get(i);
            for (CarSpeed carSpeed : list) {
                carSpeed.setTimeOrder(count);
                if(polishId) {
                    carSpeed.setId(carSpeed.getId() - timeOrder * (list.size() + 1));
                }
            }
            //因为前端显示的原因，返回时间段的编号得是连续的，所以用count而不是timeOrder
            result.add(new CarSpeedVo(count, list));
            count++;
        }
        return new Result(result);
    }

    @GetMapping("/quantity")
    public Result getTimeQuantity(@RequestParam String tableName){
        List<CarSpeed> divisionList = carSpeedService.getDivisionList(tableName);
        return new Result(divisionList.size());
    }

    private List<List<CarSpeed>> execute(int i, String tableName, List<Integer> numbers){
        System.out.println("任务"+i+"开始执行");
        List<List<CarSpeed>> lists = carSpeedService.get(tableName, numbers);
        System.out.println("任务"+i+"完成");
        return lists;
    }

    /**
     * 多分片线程查询
     * @return
     */
    private List<List<CarSpeed>> multiSelect(List<Integer> numbers,Integer pieces,String tableName) throws ExecutionException, InterruptedException {
        //片数,默认为5,最大不超过20
        pieces = pieces==null ? 5 : pieces>20 ? 20 : pieces;
        //将numbers分片
        List<Integer>[] numberPieces = NumberUtil.sub(numbers, pieces);
        //多线程
        EventLoopGroup group = new NioEventLoopGroup(pieces);
        //分配任务
        Future<List<List<CarSpeed>>>[] listPieces = new Future[pieces];
        for (int i=0;i<pieces;i++) {
            //int finalI = i;
            int finalI = i;
            listPieces[i] = group.next().submit(()-> execute(finalI,tableName,numberPieces[finalI]));
        }
        //各自完成任务后汇总
        List<List<CarSpeed>> lists = new ArrayList<>();
        for(int i=0;i<pieces;i++){
            //Future类的包装允许get()等待任务完成再执行
            lists.addAll(listPieces[i].get());
        }

        return lists;
    }


}
