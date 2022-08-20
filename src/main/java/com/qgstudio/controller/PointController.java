package com.qgstudio.controller;

import com.qgstudio.constant.ResultEnum;
import com.qgstudio.po.Point;
import com.qgstudio.service.PointService;
import com.qgstudio.util.NumberUtil;
import com.qgstudio.vo.PointVo;
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
 * @since 2022-8-14
 */
@RestController
@RequestMapping(value = "/api/point", produces = "application/json;charset=UTF-8")
public class PointController {
    @Autowired
    private PointService pointService;

    /**
     * 取出全部点集
     * @param tableName 所查的表
     * @return
     */
    @GetMapping("/all")
    public Result getAll(@RequestParam String tableName,@RequestParam(required = false) boolean polishId,@RequestParam(required = false)Integer pieces) throws ExecutionException, InterruptedException {
        //多线程查询
        int size = pointService.getDivisionList(tableName).size();
        List<Integer> numbers = new ArrayList<>();
        for(int i=1;i<=size;i++){
            numbers.add(i);
        }
        List<List<Point>> lists = multiSelect(numbers,pieces,tableName);
        //原来不分片
        //List<List<Point>> lists = pointService.get(tableName,null);
        if (lists.isEmpty()){
            return new Result(ResultEnum.ARGS_ERR);
        }
        List<PointVo> result = new ArrayList<>();
        int timeOrder = 0;
        for (List<Point> list : lists) {
            for (Point point:list) {
                point.setTimeOrder(timeOrder);
                if(polishId){
                    point.setId(point.getId()-timeOrder*(list.size()+1));
                }
            }
            result.add(new PointVo(timeOrder++,list));
        }
        return new Result(result);
    }

    /**
     * 取出指定时间段的点集
     * @param tableName 所查的表
     * @param timeOrder 指定时间段(包括0)
     * @return
     */
    @GetMapping("/timeOrder")
    public Result getByTimeOrder(@RequestParam String tableName,@RequestParam int timeOrder,@RequestParam(required = false) boolean polishId){
        List<Integer> numbers = new ArrayList<>();
        numbers.add(timeOrder+1);
        List<List<Point>> lists = pointService.get(tableName,numbers);

        List<Point> list;
        if(lists.isEmpty()){
            return new Result(400,"无此时间段");
        }else {
            list = lists.get(0);
        }

        List<PointVo> result = new ArrayList<>();
        for (Point point:list) {
            point.setTimeOrder(timeOrder);
            if(polishId) {
                point.setId(point.getId() - timeOrder * (list.size() + 1));
            }
        }
        result.add(new PointVo(timeOrder,list));

        return new Result(result);
    }

    /**
     * 通过抽帧算法,从所有点集中平均获取指定时间段数量的点集
     * @param tableName 所查的表
     * @param amount 指定时间段数量,若超出已有数据,则返回全部时间段数据
     * @return
     */
    @GetMapping
    public Result get(@RequestParam String tableName,@RequestParam int amount,@RequestParam(required = false) boolean polishId,@RequestParam(required = false)Integer pieces) throws ExecutionException, InterruptedException {
        List<Point> divisionList = pointService.getDivisionList(tableName);
        if (divisionList.isEmpty()){
            return new Result(ResultEnum.ARGS_ERR);
        }
        List<PointVo> result = new ArrayList<>();


        //总数
        int total = divisionList.size();
        //如果要求的时间段大于已有数据,直接返回全部时间段
        if(total<amount){
            amount = total;
        }
        //抽帧算法
        List<Integer> numbers = NumberUtil.frameExtracting(total, amount);

        //多线程查询
        List<List<Point>> lists = multiSelect(numbers,pieces,tableName);

        //原来不分片
        //List<List<Point>> lists = pointService.get(tableName,numbers);

        //处理
        int count = 0;
        for (int i=0;i<numbers.size();i++) {
            int timeOrder = numbers.get(i);
            //下标从0开始
            timeOrder--;
            List<Point> list = lists.get(i);
            for (Point point : list) {
                point.setTimeOrder(count);
                if(polishId) {
                    point.setId(point.getId() - timeOrder * (list.size() + 1));
                }
            }
            //因为前端显示的原因，返回时间段的编号得是连续的，所以用count而不是timeOrder
            result.add(new PointVo(count, list));
            count++;
        }
        return new Result(result);
    }

    @GetMapping("/quantity")
    public Result getTimeQuantity(@RequestParam String tableName){
        List<Point> divisionList = pointService.getDivisionList(tableName);
        return new Result(divisionList.size());
    }

    private List<List<Point>> execute(int i, String tableName, List<Integer> numbers){
        System.out.println("任务"+i+"开始执行");
        List<List<Point>> lists = pointService.get(tableName, numbers);
        System.out.println("任务"+i+"完成");
        return lists;
    }

    /**
     * 多分片线程查询
     * @return
     */
    private List<List<Point>> multiSelect(List<Integer> numbers,Integer pieces,String tableName) throws ExecutionException, InterruptedException {
        //片数,默认为5,最大不超过20
        pieces = pieces==null ? 5 : pieces>20 ? 20 : pieces;
        //将numbers分片
        List<Integer>[] numberPieces = NumberUtil.sub(numbers, pieces);
        //多线程
        EventLoopGroup group = new NioEventLoopGroup(pieces);
        //分配任务
        Future<List<List<Point>>>[] listPieces = new Future[pieces];
        for (int i=0;i<pieces;i++) {
            //int finalI = i;
            int finalI = i;
            listPieces[i] = group.next().submit(()-> execute(finalI,tableName,numberPieces[finalI]));
        }
        //各自完成任务后汇总
        List<List<Point>> lists = new ArrayList<>();
        for(int i=0;i<pieces;i++){
            //Future类的包装允许get()等待任务完成再执行
            lists.addAll(listPieces[i].get());
        }

        return lists;
    }
}
