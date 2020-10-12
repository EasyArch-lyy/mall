package com.jinxiu.malltesthutool.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/*
 * Mybatis配置类
 */
@Configuration
@MapperScan("com.jinxiu.malltesthutool.mbg.mapper")
public class MybatisConfig {
}
