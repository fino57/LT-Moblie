package com.example.sharedpreference.ui.theme

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Định nghĩa class User thay vì dùng Pair<String, String>
data class User(val username: String, val password: String)

class PreferenceHelper(context: Context) {
    companion object {
        private const val PREF_NAME = "UserPrefs"
        private const val KEY_USER_LIST = "USER_LIST"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    // Lấy danh sách user từ SharedPreferences
    fun getUserDataList(): List<User> {
        val jsonString = sharedPreferences.getString(KEY_USER_LIST, null) ?: return emptyList()
        val type = object : TypeToken<List<User>>() {}.type
        return gson.fromJson(jsonString, type)
    }

    // Lưu user vào SharedPreferences, kiểm tra trùng lặp trước khi thêm
    fun saveUserData(username: String, password: String): Boolean {
        val userList = getUserDataList().toMutableList()

        // Kiểm tra nếu username đã tồn tại
        if (userList.any { it.username == username }) {
            return false
        }

        userList.add(User(username, password)) // Thêm user mới

        val jsonString = gson.toJson(userList) // Chuyển danh sách thành JSON
        sharedPreferences.edit().putString(KEY_USER_LIST, jsonString).apply()
        return true
    }

    // Xóa toàn bộ danh sách
    fun clearUserData() {
        sharedPreferences.edit().remove(KEY_USER_LIST).apply()
    }
}
