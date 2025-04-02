package com.example.mysqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "contacts.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "contacts"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PHONE = "phone"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_NAME TEXT PRIMARY KEY, $COLUMN_PHONE TEXT)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addContact(name: String, phone: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_PHONE, phone)
        }
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result != -1L
    }

    fun updateContact(name: String, phone: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PHONE, phone)
        }
        val result = db.update(TABLE_NAME, values, "$COLUMN_NAME=?", arrayOf(name))
        db.close()
        return result > 0
    }

    fun deleteContact(name: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_NAME=?", arrayOf(name))
        db.close()
        return result > 0
    }

    fun getAllContacts(): String {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val stringBuilder = StringBuilder()
        while (cursor.moveToNext()) {
            val name = cursor.getString(0)
            val phone = cursor.getString(1)
            stringBuilder.append("Tên: $name, SĐT: $phone\n")
        }
        cursor.close()
        db.close()
        return stringBuilder.toString()
    }
}