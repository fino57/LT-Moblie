package com.example.firebaserealtime

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnLogin: Button
    private lateinit var btnShowData: Button
    private lateinit var tvData: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo Firebase Authentication và Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Ánh xạ view
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        btnRegister = findViewById(R.id.btnRegister)
        btnLogin = findViewById(R.id.btnLogin)
        btnShowData = findViewById(R.id.btnShowData)
        tvData = findViewById(R.id.tvData)

        // Xử lý sự kiện Đăng ký
        btnRegister.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            registerUser(email, password)
        }

        // Xử lý sự kiện Đăng nhập
        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            loginUser(email, password)
        }

        // Xử lý sự kiện Hiển thị dữ liệu
        btnShowData.setOnClickListener {
            showUserData()
        }
    }

    // Hàm đăng ký người dùng
    private fun registerUser(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        val user = mapOf("email" to email, "password" to password)
                        database.child("users").child(userId).setValue(user)
                        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Đăng ký thất bại: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Hàm đăng nhập người dùng
    private fun loginUser(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Đăng nhập thất bại: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Hàm hiển thị dữ liệu từ Firebase Database
    private fun showUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child("users").child(userId).get()
                .addOnSuccessListener { dataSnapshot ->
                    val email = dataSnapshot.child("email").value.toString()
                    val password = dataSnapshot.child("password").value.toString()
                    tvData.text = "Email: $email\nMật khẩu: $password"
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show()
                }
        } else {
            tvData.text = "Bạn chưa đăng nhập!"
        }
    }
}