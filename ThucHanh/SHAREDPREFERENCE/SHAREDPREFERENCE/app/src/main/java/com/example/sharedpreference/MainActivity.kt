package com.example.sharedpreference

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import com.example.sharedpreference.ui.theme.PreferenceHelper

class MainActivity : AppCompatActivity() {
    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonDelete: Button
    private lateinit var buttonShow: Button
    private lateinit var textViewDisplay: TextView
    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ánh xạ UI
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonSave = findViewById(R.id.buttonSave)
        buttonDelete = findViewById(R.id.buttonDelete)
        buttonShow = findViewById(R.id.buttonShow)
        textViewDisplay = findViewById(R.id.textViewDisplay)

        // Khởi tạo SharedPreferences Helper
        preferenceHelper = PreferenceHelper(this)

        // Xử lý sự kiện nút "Lưu"
        buttonSave.setOnClickListener {
            val username = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
            } else {
                val isSaved = preferenceHelper.saveUserData(username, password)
                if (isSaved) {
                    Toast.makeText(this, "Lưu thành công!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Tên người dùng đã tồn tại!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Xử lý sự kiện nút "Hiển thị"
        buttonShow.setOnClickListener {
            val userList = preferenceHelper.getUserDataList()
            if (userList.isEmpty()) {
                textViewDisplay.text = "Chưa có dữ liệu!"
            } else {
                val userText = userList.joinToString("\n") { "👤 ${it.username} - 🔒 ${it.password}" }
                textViewDisplay.text = userText
            }
        }

        // Xử lý sự kiện nút "Xóa"
        buttonDelete.setOnClickListener {
            preferenceHelper.clearUserData()
            textViewDisplay.text = "" // Xóa luôn nội dung hiển thị
            Toast.makeText(this, "Xóa dữ liệu thành công!", Toast.LENGTH_SHORT).show()
        }
    }
}
