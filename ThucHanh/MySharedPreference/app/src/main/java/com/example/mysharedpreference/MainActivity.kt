package com.example.mysharedpreference

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var edtUsername: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button
    private lateinit var btnShow: Button
    private lateinit var txtResult: TextView
    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ánh xạ View
        edtUsername = findViewById(R.id.edtUsername)
        edtPassword = findViewById(R.id.edtPassword)
        btnSave = findViewById(R.id.btnSave)
        btnDelete = findViewById(R.id.btnDelete)
        btnShow = findViewById(R.id.btnShow)
        txtResult = findViewById(R.id.txtResult)

        // Khởi tạo PreferenceHelper
        preferenceHelper = PreferenceHelper(this)

        // Xử lý nút "Lưu"
        btnSave.setOnClickListener {
            val username = edtUsername.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                preferenceHelper.saveUserData(username, password)
                txtResult.text = "Đã lưu thành công!"
            } else {
                txtResult.text = "Vui lòng nhập đầy đủ thông tin."
            }
        }

        // Xử lý nút "Xóa"
        btnDelete.setOnClickListener {
            preferenceHelper.clearUserData()
            txtResult.text = "Dữ liệu đã được xóa!"
        }

        // Xử lý nút "Hiển thị"
        btnShow.setOnClickListener {
            val (username, password) = preferenceHelper.getUserData()
            txtResult.text = "Tên: $username\nMật khẩu: $password"
        }
    }
}