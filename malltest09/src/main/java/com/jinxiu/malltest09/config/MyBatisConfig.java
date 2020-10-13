package com.jinxiu.malltest09.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 */
@Configuration
@MapperScan({"com.jinxiu.malltest09.mbg.mapper","com.jinxiu.malltest09.dao"})
public class MyBatisConfig {
}
