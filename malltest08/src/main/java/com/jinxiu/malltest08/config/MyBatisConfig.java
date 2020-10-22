package com.jinxiu.malltest08.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 */
@Configuration
@MapperScan({"com.jinxiu.malltest08.mbg.mapper", "com.jinxiu.malltest08.dao"})
public class MyBatisConfig {
}
