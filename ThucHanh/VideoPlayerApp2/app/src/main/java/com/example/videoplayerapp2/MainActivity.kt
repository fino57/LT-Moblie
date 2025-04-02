package com.example.videoplayerapp2

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var editTextUrl: EditText
    private val PICK_VIDEO_REQUEST = 1
    private val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        videoView = findViewById(R.id.videoView)
        editTextUrl = findViewById(R.id.editTextUrl)

        val mediaController = MediaController(this)
        videoView.setMediaController(mediaController)
        mediaController.setAnchorView(videoView)

        findViewById<Button>(R.id.buttonPick).setOnClickListener {
            pickVideoFromMediaStore()
        }

        findViewById<Button>(R.id.buttonPlayUrl).setOnClickListener {
            playVideoFromUrl()
        }
    }

    private fun pickVideoFromMediaStore() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_READ_EXTERNAL_STORAGE
            )
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_VIDEO_REQUEST)
        }
    }

    private fun playVideoFromUrl() {
        val videoUrl = editTextUrl.text.toString()
        if (videoUrl.isNotEmpty()) {
            try {
                val videoUri = Uri.parse(videoUrl)
                videoView.setVideoURI(videoUri)
                videoView.start()
            } catch (e: Exception) {
                Toast.makeText(this, "URL video không hợp lệ", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Vui lòng nhập URL video", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val videoUri = data.data
            videoView.setVideoURI(videoUri)
            videoView.start()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            pickVideoFromMediaStore()
        } else {
            Toast.makeText(this, "Quyền truy cập bộ nhớ bị từ chối", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        super.onStop()
        videoView.stopPlayback()
    }
}