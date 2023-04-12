package com.example.thelastfront.ui.main

import com.example.thelastfront.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.thelastfront.databinding.FragmentNotificationsBinding
import org.eclipse.paho.client.mqttv3.*
import java.util.*

class NotificationsFragment : Fragment() {

    private lateinit var mqttClient: IMqttClient

    private lateinit var binding: FragmentNotificationsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mqttClient = MqttClient("tcp://broker.hivemq.com:1883", MqttClient.generateClientId())
        mqttClient.connect()

        mqttClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                mqttClient.subscribe("weather", 0)
                mqttClient.subscribe("traffic", 0)
            }

            override fun connectionLost(cause: Throwable?) {}

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                activity?.runOnUiThread {
                    val currentText = binding.notificationText.text.toString()
                    val newText = "$currentText\n${message?.toString()}"
                    binding.notificationText.text = newText
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {}
        })

        Timer().schedule(object : TimerTask() {
            override fun run() {
                mqttClient.disconnect()
            }
        }, 60000)
    }

    override fun onDestroy() {
        mqttClient.disconnect()
        super.onDestroy()
    }

}
