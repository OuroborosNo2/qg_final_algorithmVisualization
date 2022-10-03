package com.qgstudio.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgstudio.po.Car;
import com.qgstudio.po.Point;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author OuroborosNo2
 * @since 2022-10-3
 */
@Mapper
public interface CarMapper extends BaseMapper<Car> {

}
