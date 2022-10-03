package com.qgstudio.service;

import com.qgstudio.po.Car;
import com.qgstudio.po.Point;

import java.util.List;

/**
 * @author OuroborosNo2
 * @since 2022-10-3
 */
public interface CarService {
    List<List<Car>> get(String tableName, List<Integer> numbers);

    List<Car> getDivisionList(String tableName);

    List<List<Car>> getBetween(String tableName, List<Car> divisionList, List<Integer> numbers);

}
