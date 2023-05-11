package com.notificationapp.service.notificationmanager.configuration;


import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationExchangeConfiguration {
	public static final String TRAFFIC_QUEUE = "traffic";
	public static final String EXCHANGE_TRAFFIC = "traffic_exchange";
	public static final String ROUTING_KEY = "traffic_routingKey";

	public static final String WEATHER_QUEUE = "weather";
	public static final String EXCHANGE_WEATHER = "traffic_exchange";
	public static final String WEATHER_ROUTING_KEY = "weather_routingKey";

	@Bean
	public Queue trafficQueue() {
		return new Queue(TRAFFIC_QUEUE);
	}

	public Queue weatherQueue() {
		return new Queue(WEATHER_QUEUE);
	}

}
