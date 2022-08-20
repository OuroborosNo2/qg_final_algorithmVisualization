package com.qgstudio;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.qgstudio.config.MybatisPlusConfig;
import com.qgstudio.mapper.PointMapper;
import com.qgstudio.service.HttpService;
import com.qgstudio.util.NumberUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class QgFinalAlgorithmApplicationTests {

    @Autowired
    private PointMapper pointMapper;
    @Autowired
    private HttpService httpService;

    @Test
    void contextLoads() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);
        list.add(9);
        list.add(10);
        list.add(11);
        for (List<Integer> integers : NumberUtil.sub(list, 1)) {
            System.out.println(integers);
        }
    }

    @Test
    void testHttp(){
        Map<String,Object> map = new HashMap<>();
        map.put("username","ouroboros");
        map.put("password","123456aA");
        System.out.println(httpService.sendRequest("http://127.0.0.1:8080/users",map));
    }

    @Test
    void testDynamicTableName(){
        MybatisPlusConfig.setDynamicTableName("custom_location");
        System.out.println(pointMapper.selectById(1));
    }


    @Test
    void testAverage() {
            System.out.println("---------------------------------");
            int total = 20;
            int target = 20;

            int step = (total - 1) / (target - 1);
            int count = 0;
            List<Integer> list = new ArrayList();
            for (int i = 1; i <= total-1; i += step) {
                count++;
                list.add(i);
            }
            System.out.println("取数" + count);
            List<Integer> range = new ArrayList<>(list);
            range.remove(1);
            Set<Integer> set = RandomUtil.randomEleSet(range, count - (target - 1));
            for (Integer i : set) {
                list.remove(i);
                System.out.println("移除了" + i);
            }

            list.add(total);
            count = 1;
            for (Integer i : list) {
                System.out.println(i + "---count:" + count++);
            }

    }

}
