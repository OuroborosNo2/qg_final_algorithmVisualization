package com.qgstudio.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qgstudio.config.MybatisPlusConfig;
import com.qgstudio.mapper.AdjacencyMapper;
import com.qgstudio.po.Adjacency;
import com.qgstudio.service.AdjacencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author OuroborosNo2
 * @since 2022-8-14
 */
@Service
public class AdjacencyServiceImpl implements AdjacencyService {
    @Autowired
    private AdjacencyMapper adjacencyMapper;

    @Override
    public List<List<Adjacency>> get(String tableName, List<Integer> numbers) {
        //分割点
        List<Adjacency> divisionList = getDivisionList(tableName);

        //用分割点来分割数据集为一个个List，考虑到可能存在每组大小不同的情况，所以不用Page分页
        return getBetween(tableName,divisionList,numbers);
    }

    @Override
    public List<Adjacency> getDivisionList(String tableName){
        tableName += "_adjacency";
        MybatisPlusConfig.setDynamicTableName(tableName);
        QueryWrapper<Adjacency> qw = new QueryWrapper<>();
        qw.isNull("ad_index");
        //分割点
        return adjacencyMapper.selectList(qw);
    }

    @Override
    public List<List<Adjacency>> getBetween(String tableName,List<Adjacency> divisionList,List<Integer> numbers){
        tableName += "_adjacency";
        MybatisPlusConfig.setDynamicTableName(tableName);
        List<List<Adjacency>> result = new ArrayList<>();
        QueryWrapper<Adjacency> qw = new QueryWrapper<>();
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
            result.add(adjacencyMapper.selectList(qw));
        }
        return result;
    }
}
