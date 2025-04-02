package com.example.stopwatchapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvTimer: TextView
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button

    private var timeElapsed = 0 // Biến đếm giây
    private var isRunning = false // Kiểm tra trạng thái bộ đếm

    private val handler = Handler(Looper.getMainLooper()) // Handler chạy trên UI thread
    private val runnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                timeElapsed++
                tvTimer.text = timeElapsed.toString()
                handler.postDelayed(this, 1000) // Lặp lại sau 1 giây
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTimer = findViewById(R.id.tvTimer)
        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)

        btnStart.setOnClickListener {
            if (!isRunning) {
                isRunning = true
                handler.post(runnable) // Bắt đầu đếm giờ
            }
        }

        btnStop.setOnClickListener {
            isRunning = false
            handler.removeCallbacks(runnable) // Dừng đếm giờ
        }
    }
}
