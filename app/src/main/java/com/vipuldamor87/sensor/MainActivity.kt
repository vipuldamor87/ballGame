package com.vipuldamor87.sensor

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener2
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), SensorEventListener2 {
    private var xPos = 0f
    private var xAccel = 0f
    private var xVel = 0.0f
    private var yPos = 0f
    private var yAccel = 0f
    private var yVel = 0.0f
    private var xMax = 0f
    private var yMax = 0f
    lateinit var ball: Bitmap
    private var sensorManager: SensorManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val size = Point()
        val display = windowManager.defaultDisplay
        display.getSize(size)
        xMax = size.x.toFloat() - 100
        yMax = size.y.toFloat() - 350
        val ballView= BallView(this)
        setContentView(ballView)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
    }

    override fun onStart() {
        super.onStart()
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onStop() {
        sensorManager!!.unregisterListener(this)
        super.onStop()
    }

    override fun onFlushCompleted(sensor: Sensor) {}
    override fun onSensorChanged(sensorEvent: SensorEvent) {
        if (sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            xAccel = sensorEvent.values[0]
            yAccel = -sensorEvent.values[1]
            Log.d("mytag","$xAccel $yAccel")
            Log.d("mytag3","xmax = $yMax $xPos $yPos")

        }
    }

    suspend fun updateBall() {
        val frameTime = 0.2f
        xVel += xAccel * frameTime
        yVel += yAccel * frameTime
        val xS = xVel / 2 * frameTime
        val yS = yVel / 2 * frameTime
        xPos -= xS
        yPos -= yS
        if (xPos > xMax) {
            xPos = xMax
        } else if (xPos < 0) {
            xPos = 0f
        }
        if (yPos > yMax) {
            yPos = yMax
        } else if (yPos < 0) {
            yPos = 0f
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, i: Int) {}
    private inner class BallView(context: Context?) : View(context) {

        override fun onDraw(canvas: Canvas) {
            canvas.drawBitmap(ball, xPos, yPos, null)
            invalidate()
        }

        init {
            val ballSrc = BitmapFactory.decodeResource(resources, R.drawable.ball)
            val dstWidth = 100
            val dstHeight = 100
            ball = Bitmap.createScaledBitmap(ballSrc, dstWidth, dstHeight, true)
        }
    }
}