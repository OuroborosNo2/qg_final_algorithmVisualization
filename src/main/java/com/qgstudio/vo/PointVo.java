package com.qgstudio.vo;

import com.qgstudio.po.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author OuroborosNo2
 * @since 2022-8-14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointVo {
    private int timeOrder;
    private List<Point> list;
}
