package com.notificationapp.service.notificationservice.consumer;

import com.notificationapp.service.notificationservice.client.APKMessagingService;
import com.notificationapp.service.notificationservice.configuration.Localisation;
import com.notificationapp.service.notificationservice.configuration.NotificationConfiguration;

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
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class NotificationConsumer {

	private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

	@Autowired
	private RabbitTemplate template;

	@Autowired
	private APKMessagingService messagingService;

	String queueName;
	String weatherQueue;
	Localisation localisation = Localisation.CRETEIL;

	public NotificationConsumer() {
		this.queueName = NotificationConfiguration.TRAFFIC_QUEUE;
		this.weatherQueue = NotificationConfiguration.WEATHER_QUEUE;
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		AmqpAdmin admin = new RabbitAdmin(connectionFactory);
		admin.declareQueue(new Queue(queueName));
		admin.declareQueue(new Queue(weatherQueue));
		template = new RabbitTemplate(connectionFactory);
	}

	@RabbitListener(queues = NotificationConfiguration.TRAFFIC_QUEUE)
	public void receiveTrafficState(String receiveNot) throws MqttException {
		log.info("Received traffic update : " + receiveNot);
		final String topic = "trafficState";
		// if (!receiveNot.equals(" Normal traffic in A ")) {
		messagingService.publishMqttTrafficState(topic, "Pertubed traffic in A", 0, true);
		// }
	}

	@RabbitListener(queues = NotificationConfiguration.WEATHER_QUEUE)
	public void receiveWeatherState(String receiveNot) throws MqttException {
		log.info("Received weather update : " + receiveNot);
		final String topic = "weatherState2";
		messagingService.publishMqttWeatherState(topic, "Warning severe weather incoming !", 0, true);
		if (receiveNot.equals("Weather condition back to normal " + localisation)) {
			messagingService.publishMqttWeatherState(topic, "Weather condition back to normal !", 0, true);
		}
	}

}