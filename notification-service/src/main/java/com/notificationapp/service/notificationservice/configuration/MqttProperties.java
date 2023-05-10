package com.notificationapp.service.notificationservice.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.mqtt")
public class MqttProperties {
	private String host;
	private String clientId;
	private String username;
	private char[] password;
	private String topicWeather;
	private String topicTraffic;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;

	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public char[] getPassword() {
		return password;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}

	public String getTopicWeather() {
		return topicWeather;
	}

	public void setTopicWeather(String topicWeather) {
		this.topicWeather = topicWeather;
	}

	public String getTopicTraffic() {
		return topicTraffic;
	}

	public void setTopicTraffic(String topicTrrafic) {
		this.topicTraffic = topicTrrafic;
	}

}