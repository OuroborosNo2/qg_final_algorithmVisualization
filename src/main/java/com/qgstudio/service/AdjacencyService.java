package com.qgstudio.service;

import com.qgstudio.po.Adjacency;
import com.qgstudio.po.Point;

import java.util.List;

/**
 * @author OuroborosNo2
 * @since 2022-8-14
 */
public interface AdjacencyService {
    List<List<Adjacency>> get(String tableName, List<Integer> numbers);

    List<Adjacency> getDivisionList(String tableName);

    List<List<Adjacency>> getBetween(String tableName, List<Adjacency> divisionList, List<Integer> numbers);
}
