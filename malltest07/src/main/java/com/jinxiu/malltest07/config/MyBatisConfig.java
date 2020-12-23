package com.jinxiu.malltest07.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 */
@Configuration
@MapperScan({"com.jinxiu.malltest07.mbg.mapper","com.jinxiu.malltest07.dao"})
public class MyBatisConfig {
}
