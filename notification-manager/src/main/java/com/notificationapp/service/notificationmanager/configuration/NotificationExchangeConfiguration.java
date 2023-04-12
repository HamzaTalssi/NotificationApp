package com.notificationapp.service.notificationmanager.configuration;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationExchangeConfiguration {
	public static final String QUEUE = "traffic_queue";
	public static final String EXCHANGE_TRAFFIC = "traffic_exchange";
	public static final String ROUTING_KEY = "traffic_routingKey";

	public static final String WEATHER_QUEUE = "weather_queue";
	public static final String EXCHANGE_WEATHER = "traffic_exchange";
	public static final String WEATHER_ROUTING_KEY = "weather_routingKey";

	@Bean
	public Queue trafficQueue() {
		// The first parameter is the name of the message queue ; The second parameter
		// indicates whether the message is persistent ; The third parameter indicates
		// whether the message queue is exclusive
		// The fourth parameter indicates that if there are no subscribed consumers in
		// the queue , The queue is automatically deleted , It is generally applicable
		// to temporary queues
		return new Queue(QUEUE, true, false, false);
	}

	
	public Queue weatherQueue() {
		return new Queue(WEATHER_QUEUE);
	}

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(EXCHANGE_TRAFFIC);
	}

	@Bean
	public Binding trafficBinding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
	}

	@Bean
	public TopicExchange exchangeWeather() {
		return new TopicExchange(EXCHANGE_WEATHER);
	}

	@Bean
	public Binding weatherBinding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(WEATHER_ROUTING_KEY);
	}

	@Bean
	public MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public AmqpTemplate template(ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(messageConverter());
		return rabbitTemplate;
	}
}
