package com.example.notificationapp;

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.RemoteViews
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.notificationapp.R
import org.eclipse.paho.client.mqttv3.*
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var mqttClient: MqttClient

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create the notification channel
        createNotificationChannel()

        findViewById<Button>(R.id.subscribe_weather_button).setOnClickListener {
            val currentTime = LocalTime.now()
            val sec = currentTime.second
            val min = currentTime.minute
            val hour = currentTime.hour
            Log.d(TAG, "Current time :$hour h $min min $sec s")
            subscribeToTopic("weatherState94")
        }

        findViewById<Button>(R.id.subscribe_traffic_button).setOnClickListener {
            val currentTime = LocalTime.now()
            val sec = currentTime.second
            val min = currentTime.minute
            Log.d(TAG, "Current time : $min minute and $sec s")
            subscribeToTopic("trafficState94")
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_name)
            val descriptionText = getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("MQTTNotifications", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private val receivedMessages: MutableSet<String> = mutableSetOf()

    private fun subscribeToTopic(topic: String) {
        // Create an instance of the Mosquitto MQTT client and connect to the broker
        mqttClient = MqttClient("tcp://broker.hivemq.com:1883", MqttClient.generateClientId(), null)
        mqttClient.connect()

        // Subscribe to the given topic
        mqttClient.subscribe(topic, object : IMqttMessageListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun messageArrived(topic: String?, message: MqttMessage?) {
                val text = message?.payload?.toString(Charset.defaultCharset())
                Log.d(TAG, "Received message on topic $topic: $text")

                // Check if the message has already been received
                if (receivedMessages.contains(text)) {
                    Log.d(TAG, "Message already received, ignoring...")
                    return
                }

                // Add the message to the received messages set
                if (text != null) {
                    receivedMessages.add(text)
                }

                // Display the message in the scroll view
                runOnUiThread {
                    val currentText = findViewById<TextView>(R.id.text_view).text.toString()
                    val newText = "$currentText\n$text"
                    findViewById<TextView>(R.id.text_view).text = newText
                }

                // Send a notification with the message content
                sendNotification(text)
            }
        })

        // Disconnect from the broker after 5 minute
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if(mqttClient.isConnected){
                mqttClient.disconnect()
                Log.d(TAG, "Disconnected from broker")}
        }, 300 * 1000)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(message: String?) {
        // Create an intent to launch the app when the notification is clicked
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val payloadString = message.toString()
        val timeString = payloadString.substring(payloadString.length - 19)

// Show the notification with a different ID based on the message
        when (message) {
            "Warning severe weather incoming at $timeString" -> {
                val notificationId = 0
                val builder = NotificationCompat.Builder(this, "MQTTNotifications")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setContentTitle("New Message")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSmallIcon(R.drawable.ic_launcher_background)

                with(NotificationManagerCompat.from(this)) {
                    notify(notificationId, builder.build())
                }
            }
            "Weather condition back to normal at $timeString" -> {
                val notificationId = 1
                val builder = NotificationCompat.Builder(this, "MQTTNotifications")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setContentTitle("New Message")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSmallIcon(R.drawable.ic_launcher_background)

                with(NotificationManagerCompat.from(this)) {
                    notify(notificationId, builder.build())
                }
            }
            "Perturbed traffic in A at $timeString" -> {
                val notificationId = 2
                val builder = NotificationCompat.Builder(this, "MQTTNotifications")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setContentTitle("New message")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSmallIcon(R.drawable.ic_launcher_background)

                with(NotificationManagerCompat.from(this)) {
                    notify(notificationId, builder.build())
                }
            }
            else -> {
                val notificationId = 3
                val builder = NotificationCompat.Builder(this, "MQTTNotifications")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setContentTitle("New message")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSmallIcon(R.drawable.ic_launcher_background)

                with(NotificationManagerCompat.from(this)) {
                    notify(notificationId, builder.build())
                }
            }
        }

    }
}
