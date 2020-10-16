package com.jinxiu.malltestrabbitmq.topic;

import cn.hutool.core.thread.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.util.StopWatch;
import sun.usagetracker.UsageTrackerClient;

public class TopicReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopicReceiver.class);

    @RabbitListener
    public void receive1(String in){
        receive(in, 1);
    }

    public void receive(String in, int receiver){
        StopWatch watch = new StopWatch();
        watch.start();
        LOGGER.info("instance {} [x] Received '{}'", receiver, in);
        doWork(in);
        watch.stop();
        LOGGER.info("instance {} [x] Done in {}", receiver, watch.getTotalTimeSeconds());
    }

    private void doWork(String in){
        for(char ch: in.toCharArray()){
            if(ch == '.'){
                ThreadUtil.sleep(1000);
            }
        }
    }
}
