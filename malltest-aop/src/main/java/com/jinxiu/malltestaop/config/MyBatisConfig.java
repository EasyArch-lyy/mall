package com.jinxiu.malltestaop.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 */
@Configuration
@MapperScan("com.jinxiu.malltestaop.mbg.mapper")
public class MyBatisConfig {
}
