package com.jinxiu.malltest02.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.jinxiu.malltest02.mbg.mapper")
public class MybatisConfig {
}
