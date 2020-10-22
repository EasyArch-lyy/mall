package com.jinxiu.malltest06.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

@Configuration
public class EsConf {

    @Bean
    RestHighLevelClient elasticsearchClient() {

        ClientConfiguration configuration = ClientConfiguration.builder()
                .connectedTo("39.100.149.36:9200")
                //.withConnectTimeout(Duration.ofSeconds(5))
                //.withSocketTimeout(Duration.ofSeconds(3))
                //.useSsl()
                //.withDefaultHeaders(defaultHeaders)
                //.withBasicAuth(username, password)
                // ... other options
                .build();

        return RestClients.create(configuration).rest();
    }

}
