package com.qgstudio.util;

import cn.hutool.core.collection.CollUtil;
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

    /**
     * 将一个集合平均分割为多个小集合
     * @param list 要分割的集合
     * @param pieces 分割片数
     * @return 分割后的集合数组
     * @param <T>
     */
    public static <T> List<T>[] sub(List<T> list, int pieces){
        List<T>[] lists = new List[pieces];
        //每片的大小
        int perSize = list.size()/pieces;
        for (int i=0;i<pieces;i++) {
            //每一片
            List<T> piece = new ArrayList<>();
            for(int o=0;o<perSize;o++){
                piece.add(list.get(o+i*perSize));
            }
            lists[i] = piece;
        }
        //将多余的放进最后一个List
        for(int i=0;i<list.size()%pieces;i++){
            lists[pieces-1].add(list.get(pieces*perSize+i));
        }
        return lists;
    }
}
