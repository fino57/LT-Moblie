package com.example.contentproviders

import ContactAdapter
import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var listViewContacts: ListView
    private val contactsList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listViewContacts = findViewById(R.id.listViewContacts)

        // Yêu cầu quyền truy cập danh bạ
        requestContactsPermission()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            loadContacts()
        } else {
            Toast.makeText(this, "Quyền bị từ chối!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestContactsPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
                    PackageManager.PERMISSION_GRANTED -> {
                loadContacts()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }

    private fun loadContacts() {
        val projection = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
        val cursor: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )

        cursor?.use {
            if (it.count > 0) {
                while (it.moveToNext()) {
                    val name = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                    contactsList.add(name)
                }
            } else {
                Toast.makeText(this, "Danh bạ trống!", Toast.LENGTH_SHORT).show()
            }
        }

        // Sử dụng ContactAdapter để hiển thị danh sách liên hệ
        val adapter = ContactAdapter(this, contactsList)
        listViewContacts.adapter = adapter
    }
}
