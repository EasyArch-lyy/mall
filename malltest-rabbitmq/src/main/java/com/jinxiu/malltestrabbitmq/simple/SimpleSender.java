package com.jinxiu.malltestrabbitmq.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class SimpleSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSender.class);

    @Autowired
    private RabbitTemplate template;

    private static final String queueName = "simple.hello";

    public void send(){
        String message = "Hello World";
        this.template.convertAndSend(queueName, message);
        LOGGER.info("[x] Send '{}'", message);
    }
}
