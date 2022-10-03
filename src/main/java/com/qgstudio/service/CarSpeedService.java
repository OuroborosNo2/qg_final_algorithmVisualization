package com.qgstudio.service;

import com.qgstudio.po.Car;
import com.qgstudio.po.CarSpeed;
import com.qgstudio.po.Point;

import java.util.List;

/**
 * @author OuroborosNo2
 * @since 2022-10-3
 */
public interface CarSpeedService {
    List<List<CarSpeed>> get(String tableName, List<Integer> numbers);

    List<CarSpeed> getDivisionList(String tableName);

    List<List<CarSpeed>> getBetween(String tableName, List<CarSpeed> divisionList, List<Integer> numbers);

}
