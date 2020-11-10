package com.jinxiu.mallteststream.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 */
@Configuration
@MapperScan("com.jinxiu.mallteststream.mbg.mapper")
public class MyBatisConfig {
}
