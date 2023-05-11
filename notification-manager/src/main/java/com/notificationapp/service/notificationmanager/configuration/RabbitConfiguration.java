package com.notificationapp.service.notificationmanager.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.notificationapp.service.notificationmanager.mock.TrafficMock;
import com.notificationapp.service.notificationmanager.mock.WeatherMock;
import com.notificationapp.service.notificationmanager.producer.NotificationTrafficProducer;
import com.notificationapp.service.notificationmanager.producer.NotificationWeatherProducer;


@Configuration
@EnableConfigurationProperties(RabbitProperties.class)
public class RabbitConfiguration {

	@Autowired
	private RabbitProperties rabbitproperties;
	
	@Bean
	public NotificationTrafficProducer notificationTrafficProducer() {
		return new NotificationTrafficProducer(rabbitproperties);
	}
	
	@Bean
	public NotificationWeatherProducer notificationWeatherProducer() {
		return new NotificationWeatherProducer(rabbitproperties);
	}
	
	@Bean
	public TrafficMock trafficMock() {
		// Create and run the MyTimerTraffic instance in the current thread
		TrafficMock trafficMock = new TrafficMock(notificationTrafficProducer());
		Thread trafficThread = new Thread(trafficMock);
		trafficThread.start();
		return trafficMock;
	}
	@Bean
	public WeatherMock weatherMock() {
		// Create and run the WeatherAlert instance in a new thread
		WeatherMock weatherAlert = new WeatherMock(notificationWeatherProducer());
		Thread weatherThread = new Thread(weatherAlert);
		weatherThread.start();
		return weatherAlert;
	}
	

}
