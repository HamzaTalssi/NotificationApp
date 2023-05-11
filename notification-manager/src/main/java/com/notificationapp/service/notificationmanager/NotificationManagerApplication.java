package com.notificationapp.service.notificationmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.notificationapp.service.notificationmanager.mock.TrafficMock;
import com.notificationapp.service.notificationmanager.mock.WeatherMock;

@SpringBootApplication
public class NotificationManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationManagerApplication.class, args);
	}

}
