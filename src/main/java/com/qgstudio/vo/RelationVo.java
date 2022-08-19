package com.qgstudio.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author OuroborosNo2
 * @since 2022-8-14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelationVo {
    //处理成起点，终点;冗余巨大，会卡顿
    //private int start;
    //private int end;
    private int timeOrder;
    private List<AdjacencyVo> list;
}
