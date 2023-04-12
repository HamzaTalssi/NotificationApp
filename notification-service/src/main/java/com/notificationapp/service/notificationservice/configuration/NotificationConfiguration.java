package com.notificationapp.service.notificationservice.configuration;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@Configuration
public class NotificationConfiguration {

    public static final String TRAFFIC_QUEUE = "traffic_queue";
    public static final String WEATHER_QUEUE = "weather_queue";
    

    @Bean
    public Queue queue(){
        return new Queue(TRAFFIC_QUEUE);
    }

    @Bean
    public Queue weatherQueue(){
        return new Queue(WEATHER_QUEUE);
    }

}
