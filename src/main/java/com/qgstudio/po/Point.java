package com.qgstudio.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author OuroborosNo2
 * @since 2022-8-14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Point {
    @TableField(exist = false)
    private int timeOrder;
    @TableId
    private int id;
    private float x;
    private float y;
}
