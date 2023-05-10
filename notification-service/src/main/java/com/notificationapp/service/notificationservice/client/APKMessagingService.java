package com.notificationapp.service.notificationservice.client;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class APKMessagingService {

    @Autowired
    private MqttClient mqttClient;


    public void publishMqttTrafficState(final String topic,final String payload, int qos, boolean retained)
            throws MqttPersistenceException, MqttException {
   	 // Create a time object
        LocalDateTime time = LocalDateTime.now();
        // Format the time as a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timeString = time.format(formatter);
        // Create a new MqttMessage object with the string as the payload
        MqttMessage mqttMessage = new MqttMessage();
        // Concatenate the time object to the existing byte array payload
        byte[] existingPayload = payload.getBytes() ;
        byte[] timePayload = timeString.getBytes();
        byte[] newPayload = new byte[existingPayload.length + timePayload.length];
        System.arraycopy(existingPayload, 0, newPayload, 0, existingPayload.length);
        System.arraycopy(timePayload, 0, newPayload, existingPayload.length, timePayload.length);
        mqttMessage.setPayload(newPayload);
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        mqttClient.publish(topic, mqttMessage);
       // mqttClient.disconnect();
    }
    
    public void publishMqttWeatherState(final String topic,final String payload, int qos, boolean retained)
            throws MqttPersistenceException, MqttException {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timeString = time.format(formatter);
        MqttMessage mqttMessage = new MqttMessage();
        byte[] existingPayload = payload.getBytes() ;
        byte[] timePayload = timeString.getBytes();
        byte[] newPayload = new byte[existingPayload.length + timePayload.length];
        System.arraycopy(existingPayload, 0, newPayload, 0, existingPayload.length);
        System.arraycopy(timePayload, 0, newPayload, existingPayload.length, timePayload.length);
        mqttMessage.setPayload(newPayload);
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        mqttClient.publish(topic, mqttMessage);
       // mqttClient.disconnect();
    }

}