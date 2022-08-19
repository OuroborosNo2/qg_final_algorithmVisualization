package com.qgstudio.service;

import com.qgstudio.po.Point;

import java.util.List;

/**
 * @author OuroborosNo2
 * @since 2022-8-14
 */
public interface PointService {
    List<List<Point>> get(String tableName, List<Integer> numbers);

    List<Point> getDivisionList(String tableName);

    List<List<Point>> getBetween(String tableName, List<Point> divisionList, List<Integer> numbers);
}
