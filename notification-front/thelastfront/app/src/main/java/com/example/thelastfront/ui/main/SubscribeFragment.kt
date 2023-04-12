package com.example.thelastfront.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.thelastfront.R
import org.eclipse.paho.client.mqttv3.*

class SubscribeFragment : Fragment() {

    private lateinit var mqttClient: IMqttClient
    private lateinit var topicWeather: EditText
    private lateinit var topicTraffic: EditText
    private lateinit var subscribeButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_subscribe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mqttClient = MqttClient("tcp://broker.hivemq.com:1883", MqttClient.generateClientId())
        mqttClient.connect()

        topicWeather = view.findViewById(R.id.topic_weather)
        topicTraffic = view.findViewById(R.id.topic_traffic)
        subscribeButton = view.findViewById(R.id.subscribe_button)

        subscribeButton.setOnClickListener {
            val weatherTopic = topicWeather.text.toString()
            val trafficTopic = topicTraffic.text.toString()

            mqttClient.subscribe(weatherTopic, 0)
            mqttClient.subscribe(trafficTopic, 0)
        }
    }

    override fun onDestroy() {
        mqttClient.disconnect()
        super.onDestroy()
    }
}

