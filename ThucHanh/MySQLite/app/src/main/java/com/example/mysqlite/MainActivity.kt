package com.example.mysqlite

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)

        val edtName = findViewById<EditText>(R.id.edtName)
        val edtPhone = findViewById<EditText>(R.id.edtPhone)
        val txtResult = findViewById<TextView>(R.id.txtResult)

        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnDelete = findViewById<Button>(R.id.btnDelete)
        val btnShow = findViewById<Button>(R.id.btnShow)

        btnAdd.setOnClickListener {
            val name = edtName.text.toString().trim()
            val phone = edtPhone.text.toString().trim()
            if (databaseHelper.addContact(name, phone)) {
                Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show()
            }
        }

        btnUpdate.setOnClickListener {
            val name = edtName.text.toString().trim()
            val phone = edtPhone.text.toString().trim()
            if (databaseHelper.updateContact(name, phone)) {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show()
            }
        }

        btnDelete.setOnClickListener {
            val name = edtName.text.toString().trim()
            if (databaseHelper.deleteContact(name)) {
                Toast.makeText(this, "Xóa thành công!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Xóa thất bại!", Toast.LENGTH_SHORT).show()
            }
        }

        btnShow.setOnClickListener {
            txtResult.text = databaseHelper.getAllContacts()
        }
    }
}