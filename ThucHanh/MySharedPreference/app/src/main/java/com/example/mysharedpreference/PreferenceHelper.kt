package com.example.mysharedpreference

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    // Lưu dữ liệu vào SharedPreferences
    fun saveUserData(username: String, password: String) {
        sharedPreferences.edit().apply {
            putString("username", username)
            putString("password", password)
            apply()  // Lưu thay đổi
        }
    }

    // Lấy dữ liệu từ SharedPreferences
    fun getUserData(): Pair<String, String> {
        val username = sharedPreferences.getString("username", "Chưa có dữ liệu") ?: "Chưa có dữ liệu"
        val password = sharedPreferences.getString("password", "Chưa có dữ liệu") ?: "Chưa có dữ liệu"
        return Pair(username, password)
    }

    // Xóa dữ liệu đã lưu
    fun clearUserData() {
        sharedPreferences.edit().clear().apply()
    }
}