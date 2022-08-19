package com.qgstudio.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qgstudio.config.MybatisPlusConfig;
import com.qgstudio.mapper.AlgorithmMapper;
import com.qgstudio.po.Algorithm;
import com.qgstudio.service.AlgorithmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author OuroborosNo2
 * @since 2022-8-14
 */
@Service
public class AlgorithmServiceImpl implements AlgorithmService {
    @Autowired
    private AlgorithmMapper algorithmMapper;

    @Override
    public List<Algorithm> getAll() {
        QueryWrapper<Algorithm> qw = new QueryWrapper<>();
        MybatisPlusConfig.setDynamicTableName("t_algorithm");
        return algorithmMapper.selectList(qw);
    }
}
