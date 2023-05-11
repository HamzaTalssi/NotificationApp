package com.notificationapp.service.notificationmanager.mock;

import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.notificationapp.service.notificationmanager.producer.NotificationTrafficProducer;

public class TrafficMock implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(TrafficMock.class);
	private int normalDuration = 10; // normal scenario duration in seconds
	private int perturbedDuration = 1; // perturbed scenario duration in seconds
	private String location = "CRETEIL"; // location to check the traffic for

	private final NotificationTrafficProducer notificationTraffic;

	@Autowired
	public TrafficMock(NotificationTrafficProducer notificationTraffic) {
		this.notificationTraffic = notificationTraffic;
	}

	@Override
	public void run() {
		runNormalTraffic();
	}

	private void runNormalTraffic() {
		while (true) {
			log.info("[{}] Checking traffic for {} ***************TRAFFIC************* ", new Date(), location);
			boolean PerturbedTraffic = Math.random() < 0.1; // less than 10% chance of perturbed traffic conditions
															// passing
			if (PerturbedTraffic) {
				runPerturbedTraffic();
			}
			try {
				Thread.sleep(normalDuration * 1000L);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				log.error("Interrupted while running normal scenario", e);
			}
		}
	}

	private void runPerturbedTraffic() {
		while (true) {

			log.info("[{}] ALERT TRAFFIC in A ", new Date());
			notificationTraffic.publishState(" Pertubed traffic in " + location);
			try {
				Random random = new Random();
				int randNum = random.nextInt(15) + 5;
				Thread.sleep((perturbedDuration * randNum) * 1000L);
				boolean perturbedDurationRandom = Math.random() < 0.1;
				if (!perturbedDurationRandom) {
					notificationTraffic.publishState("The Traffic is back to normal in  " + location);
					log.info("[{}] Traffic back to normal in A ", new Date());
					runNormalTraffic();
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				log.error("Interrupted while running perturbed scenario", e);
			}
		}
	}
}