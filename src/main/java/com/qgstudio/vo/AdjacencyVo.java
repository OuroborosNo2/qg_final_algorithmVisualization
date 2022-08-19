package com.qgstudio.vo;

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
public class AdjacencyVo {
    private int timeOrder;
    private int id;
    private List<Integer> end;
}
