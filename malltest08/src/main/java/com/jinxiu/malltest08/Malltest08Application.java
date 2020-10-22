package com.jinxiu.malltest08;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

@SpringBootApplication
public class Malltest08Application {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Malltest08Application.class, args);
    }

}
