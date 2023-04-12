package com.notificationapp.service.notificationmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.notificationapp.service.notificationmanager.mock.TrafficMock;
import com.notificationapp.service.notificationmanager.mock.WeatherMock;

@SpringBootApplication
public class NotificationManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationManagerApplication.class, args);

		// Create and run the MyTimerTraffic instance in the current thread
		TrafficMock timer = new TrafficMock();
		Thread trafficThread = new Thread(timer);
		trafficThread.start();

		// Create and run the WeatherAlert instance in a new thread
		WeatherMock weatherAlert = new WeatherMock();
		// Thread weatherThread = new Thread(weatherAlert);
		weatherAlert.run();	}

}
