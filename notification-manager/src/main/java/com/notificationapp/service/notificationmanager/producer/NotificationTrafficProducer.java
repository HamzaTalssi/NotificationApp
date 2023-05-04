package com.notificationapp.service.notificationmanager.producer;

import java.sql.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notificationapp.service.notificationmanager.configuration.NotificationExchangeConfiguration;
import com.notificationapp.service.notificationmanager.expiration.ExpiredMessageConfiguration;

@RestController
@RequestMapping("")
public class NotificationTrafficProducer {

	private static final Logger log = LoggerFactory.getLogger(NotificationTrafficProducer.class);

	@Autowired
	private RabbitTemplate template = new RabbitTemplate();

	String queueName;

	public NotificationTrafficProducer() {
		this.queueName = NotificationExchangeConfiguration.TRAFFIC_QUEUE;
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		AmqpAdmin admin = new RabbitAdmin(connectionFactory);
		admin.declareQueue(new Queue(queueName));
		template = new RabbitTemplate(connectionFactory);
	}

	public String publishState(String state) {
		// log.info("Sending weather condition " + state);
		MessageProperties properties = new MessageProperties();
		properties.setContentType("application/octet-stream");
		properties.setTimestamp(new Date(System.currentTimeMillis()));
		Message message = new Message(state.getBytes(), properties);
		// the message will be expired after 60s if it remains in the queue
		template.convertAndSend(queueName, message, new ExpiredMessageConfiguration(60 * 1000L, template));
		return "Weather condition published";

	}

}
