package com.example.accelerometerapp
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var xValue: TextView
    private lateinit var yValue: TextView
    private lateinit var zValue: TextView
    private lateinit var ball: ImageView

    private var x = 0f
    private var y = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        xValue = findViewById(R.id.xValue)
        yValue = findViewById(R.id.yValue)
        zValue = findViewById(R.id.zValue)
        ball = findViewById(R.id.ball)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val xAcceleration = event.values[0]
            val yAcceleration = event.values[1]
            val zAcceleration = event.values[2]

            xValue.text = "X: ${xAcceleration}"
            yValue.text = "Y: ${yAcceleration}"
            zValue.text = "Z: ${zAcceleration}"

            // Mô phỏng chuyển động
            x -= xAcceleration * 10
            y += yAcceleration * 10

            // Giới hạn vị trí của quả bóng
            val maxX = (resources.displayMetrics.widthPixels - ball.width).toFloat()
            val maxY = (resources.displayMetrics.heightPixels - ball.height).toFloat()

            x = x.coerceIn(0f, maxX)
            y = y.coerceIn(0f, maxY)

            ball.x = x
            ball.y = y
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Xử lý khi độ chính xác của cảm biến thay đổi
    }
}