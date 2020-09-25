package com.jinxiu.malltest01.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.jinxiu.malltest01.mapper")
public class MyBatisConfig {
}
