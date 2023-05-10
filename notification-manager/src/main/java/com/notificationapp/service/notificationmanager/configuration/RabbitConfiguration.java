package com.notificationapp.service.notificationmanager.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.notificationapp.service.notificationmanager.producer.NotificationTrafficProducer;


@Configuration
@EnableConfigurationProperties(RabbitProperties.class)
public class RabbitConfiguration {

	@Autowired
	private RabbitProperties rabbitproperties;
	
	@Bean
	public NotificationTrafficProducer notificationTrafficProducer() {
		return new NotificationTrafficProducer(rabbitproperties);
	}
	
	
	
}
