package com.jinxiu.malltest07;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

@SpringBootApplication
public class Malltest07Application {

    public static void main(String[] args) {
        SpringApplication.run(Malltest07Application.class, args);
    }
}
