package com.qgstudio.service;

import com.qgstudio.po.Algorithm;

import java.util.List;

/**
 * @author OuroborosNo2
 * @since 2022-8-14
 */
public interface AlgorithmService {
    /**
     * 获取所有算法
     */
    List<Algorithm> getAll();
}
