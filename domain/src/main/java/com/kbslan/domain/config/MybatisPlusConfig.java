package com.kbslan.domain.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {"com.kbslan.domain.mapper"})
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 性能分析插件
     * @Profile({"local","dev"}) 表示local和dev开启 其余环境禁用
     * @return
     */
//    @Bean
//    @Profile({"local","dev"})
//    public PerformanceInterceptor performanceInterceptor() {
//        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
//        // 格式化sql输出
//        performanceInterceptor.setFormat(true);
//        // 设置sql执行最大时间，单位（ms）
//        performanceInterceptor.setMaxTime(5000L);
//
//        return performanceInterceptor;
//    }
}
