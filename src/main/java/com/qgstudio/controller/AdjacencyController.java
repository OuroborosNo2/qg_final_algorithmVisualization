package com.qgstudio.controller;

import com.qgstudio.constant.ResultEnum;
import com.qgstudio.po.Adjacency;
import com.qgstudio.service.AdjacencyService;
import com.qgstudio.util.NumberUtil;
import com.qgstudio.vo.AdjacencyVo;
import com.qgstudio.vo.RelationVo;
import com.qgstudio.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author OuroborosNo2
 * @since 2022-8-14
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/adjacency", produces = "application/json;charset=UTF-8")
public class AdjacencyController {
    @Autowired
    AdjacencyService adjacencyService;
    /**
     * 取出全部点间关系
     * @param tableName 所查的表
     * @return
     */
    @GetMapping("/all")
    public Result getAll(@RequestParam String tableName, @RequestParam(required = false) boolean polishId){
        //获取所有时间段的数据集
        List<List<Adjacency>> lists = adjacencyService.get(tableName,null);
        //外面再套一层
        List<RelationVo> result2 = new ArrayList<>();
        int timeOrder = 0;
        for (List<Adjacency> list : lists) {
            List<AdjacencyVo> result = wrapAdjacencyAsVoList(list,timeOrder,polishId);
            result2.add(new RelationVo(timeOrder++,result));
        }

        return new Result(result2);
    }
    /**
     * 取出指定时间段的点间关系
     * @param tableName 所查的表
     * @param timeOrder 指定时间段(包括0)
     * @return
     */
    @GetMapping("/timeOrder")
    public Result getByTimeOrder(@RequestParam String tableName,@RequestParam int timeOrder,@RequestParam(required = false) boolean polishId){
        List<Integer> numbers = new ArrayList<>();
        numbers.add(timeOrder+1);
        List<List<Adjacency>> lists = adjacencyService.get(tableName,numbers);

        List<Adjacency> list;
        if(lists.isEmpty()){
            return new Result(400,"无此时间段");
        }else {
            list = lists.get(0);
        }

        //外面再套一层
        List<RelationVo> result2 = new ArrayList<>();
        List<AdjacencyVo> result = wrapAdjacencyAsVoList(list,timeOrder,polishId);

        result2.add(new RelationVo(timeOrder,result));

        return new Result(result2);
    }

    /**
     * 通过抽帧算法,从所有点间关系中平均获取指定时间段数量的点间关系
     * @param tableName 所查的表
     * @param amount 指定时间段数量,若超出已有数据,则返回全部时间段数据
     * @return
     */
    @GetMapping
    public Result get(@RequestParam String tableName,@RequestParam int amount,@RequestParam(required = false) boolean polishId){
        List<Adjacency> divisionList = adjacencyService.getDivisionList(tableName);
        if (divisionList.isEmpty()){
            return new Result(ResultEnum.ARGS_ERR);
        }

        //时间段总数
        int total = divisionList.size();
        //如果要求的时间段大于已有数据,直接返回全部时间段
        if(total<amount){
            amount = total;
        }
        //抽帧算法
        List<Integer> numbers = NumberUtil.frameExtracting(total, amount);

        List<List<Adjacency>> lists = adjacencyService.get(tableName, numbers);

        //外面再套一层
        List<RelationVo> result2 = new ArrayList<>();
        //处理
        int count = 0;
        List<AdjacencyVo> result;
        for (int i=0;i<numbers.size();i++) {
            int timeOrder = numbers.get(i);
            //下标从0开始
            timeOrder--;
            result = new ArrayList<>();
            List<Adjacency> list = lists.get(i);
            for (Adjacency adjacency : list) {
                if(!"".equals(adjacency.getAdIndex())){
                    String[] split = adjacency.getAdIndex().split(", ");
                    List<Integer> end = new ArrayList<>();
                    for (String s : split) {
                        if(polishId){
                            end.add(Integer.parseInt(s));
                        }else {
                            //数据上后面时间段的点，对应的是第一时间段的点，要进行关系的纠正
                            end.add(Integer.parseInt(s)+timeOrder*(total+1));
                        }
                    }
                    int id = adjacency.getId();
                    if(polishId) {
                        //前端要求每个时间段的id都相同
                        id -= timeOrder*(total+1);
                    }
                    //因为前端显示的原因，返回时间段的编号得是连续的，所以用count而不是timeOrder
                    result.add(new AdjacencyVo(count,id,end));
                }
            }
            result2.add(new RelationVo(count,result));
            count++;
        }
        return new Result(result2);
    }

    @GetMapping("/quantity")
    public Result getTimeQuantity(@RequestParam String tableName){
        List<Adjacency> divisionList = adjacencyService.getDivisionList(tableName);
        return new Result(divisionList.size());
    }
    /**
     * 将Adjacency数组包装成AdjacencyVo数组
     * @param list
     * @param timeOrder
     * @return
     */
    public List<AdjacencyVo> wrapAdjacencyAsVoList(List<Adjacency> list, int timeOrder,boolean polishId){
        List<AdjacencyVo> result = new ArrayList<>();
        //点的总数（不分时间段）
        int total = 0;
        total = list.size();
        for (Adjacency adjacency : list) {
            if(!"".equals(adjacency.getAdIndex())){
                String[] split = adjacency.getAdIndex().split(", ");
                List<Integer> end = new ArrayList<>();
                for (String s : split) {
                    if(polishId){
                        end.add(Integer.parseInt(s));
                    }else {
                        //数据上后面时间段的点，对应的是第一时间段的点，要进行关系的纠正
                        end.add(Integer.parseInt(s)+timeOrder*(total+1));
                    }
                }
                int id = adjacency.getId();
                if(polishId) {
                    //前端要求每个时间段的id都相同
                    id -= timeOrder*(total+1);
                }
                result.add(new AdjacencyVo(timeOrder,id,end));
            }
        }
        return result;
    }
}
