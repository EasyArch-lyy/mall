package com.jinxiu.malltest06.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 */
@Configuration
@MapperScan({"com.jinxiu.malltest06.mbg.mapper","com.jinxiu.malltest06.dao"})
public class MyBatisConfig {
}
