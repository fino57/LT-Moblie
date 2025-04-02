package com.example.timerapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView timeTextView;
    private Handler handler;
    private int secondsElapsed = 0;
    private boolean isRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ TextView
        timeTextView = findViewById(R.id.timeTextView);

        // Khởi tạo Handler gắn với Main Looper (UI Thread)
        handler = new Handler(Looper.getMainLooper());

        // Tạo và chạy thread đếm giờ
        startTimerThread();
    }

    private void startTimerThread() {
        // Tạo Runnable để cập nhật UI
        Runnable updateUIRunnable = new Runnable() {
            @Override
            public void run() {
                // Cập nhật TextView trên UI Thread
                timeTextView.setText("Time elapsed: " + secondsElapsed + " seconds");

                // Lên lịch cho lần cập nhật tiếp theo sau 1 giây
                if (isRunning) {
                    handler.postDelayed(this, 1000);
                }
            }
        };

        // Tạo thread nền để đếm thời gian
        Thread timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        // Tăng thời gian mỗi giây
                        Thread.sleep(1000);
                        secondsElapsed++;

                        // Gửi Runnable tới Handler để cập nhật UI
                        handler.post(updateUIRunnable);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Bắt đầu thread
        timerThread.start();

        // Đăng ký lần cập nhật UI đầu tiên
        handler.post(updateUIRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Dừng thread khi activity bị hủy
        isRunning = false;
        handler.removeCallbacksAndMessages(null); // Xóa tất cả callback từ Handler
    }
}