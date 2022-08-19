package com.qgstudio.util;

import cn.hutool.core.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * @author OuroborosNo2
 * @since 2022-8-18
 */
public class NumberUtil {
    /**
     * 抽帧算法
     * @param total 总数
     * @param target 目标数量
     * @return
     */
    public static List<Integer> frameExtracting(int total,int target){
        //先挑出最后一个,然后计算步长
        int step = (total - 1) / (target - 1);
        //计数
        int count = 0;
        //平均取数
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= total-1; i += step) {
            count++;
            numbers.add(i);
        }
        //随机剔除超出的数据(不剔除1)
        List<Integer> range = new ArrayList<>(numbers);
        range.remove(1);
        Set<Integer> set = RandomUtil.randomEleSet(range, count - (target - 1));
        for (Integer i : set) {
            numbers.remove(i);
        }
        //加上最后一个
        numbers.add(total);
        return numbers;
    }
}
