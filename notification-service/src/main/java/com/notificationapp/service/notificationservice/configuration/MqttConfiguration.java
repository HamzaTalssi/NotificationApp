package com.notificationapp.service.notificationservice.configuration;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.notificationapp.service.notificationservice.consumer.NotificationConsumer;


@Configuration
@EnableConfigurationProperties({RabbitProperties.class,MqttProperties.class})
public class MqttConfiguration {

    @Autowired
    private MqttProperties mqttProperties;
    
    @Autowired
    private RabbitProperties rabbitProperties;

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setServerURIs(new String[]{mqttProperties.getHost()});
        mqttConnectOptions.setUserName(mqttProperties.getUsername());
        mqttConnectOptions.setPassword(mqttProperties.getPassword());
        mqttConnectOptions.setCleanSession(true);
        return mqttConnectOptions;
    }

    @Bean
    public MqttClient mqttClient() throws MqttException {
        MqttClient mqttClient = new MqttClient(mqttProperties.getHost(), mqttProperties.getClientId(),new MemoryPersistence());
        mqttClient.connect(mqttConnectOptions());
        return mqttClient;
    }
    
    @Bean
    public NotificationConsumer notificationConsumer() {
    	return new NotificationConsumer(mqttProperties, rabbitProperties);
    }


}
