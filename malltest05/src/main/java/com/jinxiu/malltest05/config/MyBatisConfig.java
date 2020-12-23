package com.jinxiu.malltest05.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 */
@Configuration
@MapperScan({"com.jinxiu.malltest05.mbg.mapper","com.jinxiu.malltest05.dao"})
public class MyBatisConfig {
}
