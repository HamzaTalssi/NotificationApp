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

//	@Bean
//	public TopicExchange exchange() {
//		return new TopicExchange(EXCHANGE_TRAFFIC);
//	}
//
//	@Bean
//	public Binding trafficBinding(Queue queue, TopicExchange exchange) {
//		return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
//	}
//
//	@Bean
//	public TopicExchange exchangeWeather() {
//		return new TopicExchange(EXCHANGE_WEATHER);
//	}
//
//	@Bean
//	public Binding weatherBinding(Queue queue, TopicExchange exchange) {
//		return BindingBuilder.bind(queue).to(exchange).with(WEATHER_ROUTING_KEY);
//	}
//
//	@Bean
//	public MessageConverter messageConverter() {
//		return new Jackson2JsonMessageConverter();
//	}
//
//	@Bean
//	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//		rabbitTemplate.setMessageConverter(messageConverter());
//		return rabbitTemplate;
//	}
}
