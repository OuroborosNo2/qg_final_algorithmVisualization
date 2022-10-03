package com.qgstudio.vo;

import com.qgstudio.po.Car;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * @author OuroborosNo2
 * @since 2022-10-3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarVo {
    private int timeOrder;
    private List<Car> list;

}
