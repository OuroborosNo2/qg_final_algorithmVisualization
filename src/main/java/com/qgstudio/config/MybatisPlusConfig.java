package com.qgstudio.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Random;

/**
 * @author OuroborosNo2
 * @since 2022-8-14
 */
@Configuration
public class MybatisPlusConfig {

    //表名
    private static ThreadLocal<String> table = new ThreadLocal<>();

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        //设置表名处理器，return的即最终表名
        dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) -> {
            return table.get();
        });
        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
        // 3.4.3.2 作废该方式
        // dynamicTableNameInnerInterceptor.setTableNameHandlerMap(map);
        return interceptor;
    }
    //设置表名
    public static void setDynamicTableName(String tableName){
        //TODO 暂时关闭DP
        if(tableName.contains("DP_")){
            tableName = tableName.replaceAll("DP_","");
        }
        table.set(tableName);
    }
}
