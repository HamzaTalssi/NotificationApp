import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.thelastfront.R
import com.example.thelastfront.ui.main.NotificationsFragment
import com.example.thelastfront.databinding.MainActivityBinding
import org.eclipse.paho.client.mqttv3.MqttClient
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, NotificationsFragment())
                .commit()
        }

        binding.subscribeWeatherButton.setOnClickListener {
            // Create an instance of the Mosquitto MQTT client and connect to the broker
            val mqttClient = MqttClient("tcp://broker.hivemq.com:1883", MqttClient.generateClientId())
            mqttClient.connect()

            // Subscribe to the weather topic
            mqttClient.subscribe("weather", 0)

            // Disconnect from the broker after 1 minute
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    mqttClient.disconnect()
                }
            }, 60000)
        }

        binding.subscribeTrafficButton.setOnClickListener {
            // Create an instance of the Mosquitto MQTT client and connect to the broker
            val mqttClient = MqttClient("tcp://broker.hivemq.com:1883", MqttClient.generateClientId())
            mqttClient.connect()

            // Subscribe to the traffic topic
            mqttClient.subscribe("traffic", 0)

            // Disconnect from the broker after 1 minute
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    mqttClient.disconnect()
                }
            }, 60000)
        }
    }
}
