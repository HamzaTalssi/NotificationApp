package com.notificationapp.service.notificationservice.consumer;

import com.notificationapp.service.notificationservice.client.APKMessagingService;
//import com.notificationapp.service.notificationservice.client.APKMessagingService;
import com.notificationapp.service.notificationservice.configuration.Localisation;
import com.notificationapp.service.notificationservice.configuration.MqttProperties;
import com.notificationapp.service.notificationservice.configuration.NotificationConfiguration;
import com.notificationapp.service.notificationservice.configuration.RabbitProperties;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;



public class NotificationConsumer {

	private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

	@Autowired
	private RabbitTemplate template;

	@Autowired
	private APKMessagingService messagingService;
		
	
	private final MqttProperties mqttProperties;
	
	
	private final RabbitProperties rabbitProperties;
	

	String queueName;
	String weatherQueue;
	Localisation localisation = Localisation.CRETEIL;

	@Autowired
	public NotificationConsumer(MqttProperties mqttProperties,RabbitProperties rabbitProperties) {
		this.queueName = NotificationConfiguration.TRAFFIC_QUEUE;
		this.weatherQueue = NotificationConfiguration.WEATHER_QUEUE;
		this.mqttProperties = mqttProperties;
		this.rabbitProperties = rabbitProperties;
		final String host = rabbitProperties.getHost();
		final int port = rabbitProperties.getPort();
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
		AmqpAdmin admin = new RabbitAdmin(connectionFactory);
		admin.declareQueue(new Queue(queueName));
		template = new RabbitTemplate(connectionFactory);
	}

	@RabbitListener(queues = NotificationConfiguration.TRAFFIC_QUEUE)
	public void receiveTrafficState(String message) throws MqttException {
		log.info("{} Received traffic update : {}", new Date(), message);
		final String topic = mqttProperties.getTopicTraffic();
		messagingService.publishMqttTrafficState(topic, "Pertubed traffic in A at ", 0, true);
		if (message.equals("The Traffic is back to normal in  " + localisation)) {
			messagingService.publishMqttTrafficState(topic, "The Traffic is back to normal at ", 0, true);
		}
	}

	@RabbitListener(queues = NotificationConfiguration.WEATHER_QUEUE)
	public void receiveWeatherState(String message) throws MqttException {
		log.info("{} Received weather update : {}", new Date(), message);
		final String topic = mqttProperties.getTopicWeather();
		messagingService.publishMqttWeatherState(topic, "Warning severe weather incoming at ", 0, true);
		if (message.equals("Weather condition back to normal " + localisation)) {
			messagingService.publishMqttWeatherState(topic, "Weather condition back to normal at ", 0, true);
		}
	}

}
