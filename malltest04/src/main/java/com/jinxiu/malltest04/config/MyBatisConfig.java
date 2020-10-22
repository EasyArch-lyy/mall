package com.jinxiu.malltest04.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 */
@Configuration
@MapperScan({"com.jinxiu.malltest04.mbg.mapper", "com.jinxiu.malltest04.dao"})
public class MyBatisConfig {
}
