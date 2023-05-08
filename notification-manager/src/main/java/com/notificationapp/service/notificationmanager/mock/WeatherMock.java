package com.notificationapp.service.notificationmanager.mock;

import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.notificationapp.service.notificationmanager.producer.NotificationWeatherProducer;

public class WeatherMock implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(WeatherMock.class);
	private int normalInterval = 10; // normal interval between weather checks in seconds
	private int severeInterval = 1; // interval between weather checks during severe weather conditions in seconds
	private String location = "CRETEIL"; // location to check the weather for

	@Autowired
	private NotificationWeatherProducer notificationWeather = new NotificationWeatherProducer();

	@Override
	public void run() {
		runNormalScenario();
	}

	private void runNormalScenario() {
		while (true) {
			log.info("[{}] Checking weather for {} ***********WEATHER************", new Date(), location);
			// Check if severe weather conditions exist randomly
			boolean severeConditions = (Math.random() < 0.1); // less than 10% chance of severe weather conditions
			if (severeConditions) {
				runSevereScenario();
			}
			try {
				Thread.sleep(normalInterval * 1000L);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				log.error("Interrupted while running normal scenario", e);
			}
		}
	}

	private void runSevereScenario() {
		while (true) {
			log.info("[{}] Severe weather alert for {}", new Date(), location);
			notificationWeather.publishWeatherAlert("Severe weather conditions in " + location);
			// Wait for severe weather conditions to pass before returning to normal
			// scenario
			try {
				Random random = new Random();
				int randNum = random.nextInt(15) + 5;
				Thread.sleep((severeInterval * randNum) * 1000L);
				boolean severeConditions = (Math.random() < 0.1); //
				if (!severeConditions) {
					notificationWeather.publishWeatherAlert("Weather condition back to normal " + location);
					log.info("[{}] Weather condition back to normal in {}", new Date(), location);
					runNormalScenario();
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				log.error("Interrupted while running severe scenario", e);
			}
		}
	}
}