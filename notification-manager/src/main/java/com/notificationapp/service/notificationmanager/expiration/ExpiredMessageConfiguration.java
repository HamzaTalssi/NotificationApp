package com.notificationapp.service.notificationmanager.expiration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class ExpiredMessageConfiguration implements MessagePostProcessor {

	private static final Logger log = LoggerFactory.getLogger(ExpiredMessageConfiguration.class);

	private final long expirationTimeInMillis;

	private final RabbitTemplate rabbitTemplate;

	public ExpiredMessageConfiguration(long expirationTimeInMillis, RabbitTemplate rabbitTemplate) {
		this.expirationTimeInMillis = expirationTimeInMillis;
		this.rabbitTemplate = rabbitTemplate;
	}

	@Override
	public Message postProcessMessage(Message message) {
	    long ageInMillis = System.currentTimeMillis() - message.getMessageProperties().getTimestamp().getTime();
	    if (ageInMillis >= expirationTimeInMillis) {
	        log.info("Discarding expired message: {}", message);
	        return null; // Discard the message
	    } else {
	        long delay = (expirationTimeInMillis - ageInMillis);
	        long seconds = delay / 1000;
	        log.info("Delaying message for {} s", seconds);
	        MessageProperties messageProperties = message.getMessageProperties();
	        messageProperties.setExpiration(String.valueOf(delay));
	        return new Message(message.getBody(), messageProperties);
	    }
	}
}

