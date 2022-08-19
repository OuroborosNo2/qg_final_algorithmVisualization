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
@NoArgsConstructor
@AllArgsConstructor
public class Adjacency {
    @TableId
    private int id;
    private String adIndex;
}
