package com.example.lanedodge1.utilities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.lanedodge1.interfaces.TiltCallback

class TiltDetector(context: Context, private val tiltCallback: TiltCallback) {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val sensor =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var timestamp: Long = 0L

    private val sensorEventListener = object : SensorEventListener {

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Pass
        }

        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            handleTilt(x)
        }
    }

    private fun handleTilt(x: Float) {
        if (System.currentTimeMillis() - timestamp >= 500) {
            timestamp = System.currentTimeMillis()

            if (x > 3) {
                tiltCallback?.tiltLeft()
            } else if (x < -3) {
                tiltCallback?.tiltRight()
            }
        }
    }


    fun start() {
        sensorManager.registerListener(
            sensorEventListener,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    fun stop() {
        sensorManager.unregisterListener(
            sensorEventListener,
            sensor
        )
    }
}
