package com.mall.malltestredis.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 */
@Configuration
@MapperScan("com.jinxiu.malltest.mbg.mapper")
public class MyBatisConfig {
}
