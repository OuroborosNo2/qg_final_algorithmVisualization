package com.qgstudio.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qgstudio.config.MybatisPlusConfig;
import com.qgstudio.mapper.PointMapper;
import com.qgstudio.po.Point;
import com.qgstudio.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author OuroborosNo2
 * @since 2022-8-14
 */
@Service
public class PointServiceImpl implements PointService {
    @Autowired
    private PointMapper pointMapper;

    @Override
    public List<List<Point>> get(String tableName, List<Integer> numbers) {
        //分割点
        List<Point> divisionList = getDivisionList(tableName);
        return getBetween(tableName,divisionList,numbers);
    }

    @Override
    public List<Point> getDivisionList(String tableName){
        tableName += "_location";
        MybatisPlusConfig.setDynamicTableName(tableName);
        QueryWrapper<Point> qw = new QueryWrapper<>();
        qw.isNull("x").isNull("y");
        //分割点
        return pointMapper.selectList(qw);
    }
    @Override
    public List<List<Point>> getBetween(String tableName, List<Point> divisionList, List<Integer> numbers){
        tableName += "_location";
        MybatisPlusConfig.setDynamicTableName(tableName);
        List<List<Point>> result = new ArrayList<>();
        QueryWrapper<Point> qw = new QueryWrapper<>();
        if(numbers == null){
            //如果未指定时间段数量，则查询全部时间段
            numbers = new ArrayList<>();
            for(int i=1;i<=divisionList.size();i++){
                numbers.add(i);
            }
        }
        int left;
        int right;
        //用分割点来分割点集为一个个List，考虑到可能存在每组大小不同的情况，所以不用Page分页
        for(int i=0;i<numbers.size();i++){
            //目标时间段
            int targetTime = numbers.get(i)-1;
            //如果是第0时间段
            if(targetTime == 0){
                //-1是为了包括0的查询
                left = -1;
            }else{
                left = divisionList.get(targetTime-1).getId();
            }
            right = divisionList.get(targetTime).getId();
            qw.clear();
            qw.between("id",left+1,right-1);
            result.add(pointMapper.selectList(qw));
        }
        return result;
    }
}
