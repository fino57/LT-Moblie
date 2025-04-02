package com.example.audiorecorderapp

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import java.io.IOException

class MainActivity : AppCompatActivity() {

    // Biến quản lý MediaRecorder (ghi âm) và MediaPlayer (phát lại)
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var fileName: String = "" // Đường dẫn file ghi âm

    // Danh sách lưu trữ các bản ghi âm
    private val recordings = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ánh xạ các thành phần giao diện
        val btnRecord = findViewById<Button>(R.id.btnRecord)
        val btnPlay = findViewById<Button>(R.id.btnPlay)
        val listView = findViewById<ListView>(R.id.listView)

        // Kiểm tra quyền, nếu chưa có thì yêu cầu người dùng cấp
        if (!checkPermissions()) {
            requestPermissions()
        }

        // Sự kiện bấm nút "Bắt đầu ghi âm"
        btnRecord.setOnClickListener {
            if (mediaRecorder == null) {
                startRecording() // Bắt đầu ghi âm
                btnRecord.text = "Dừng ghi âm"
            } else {
                stopRecording() // Dừng ghi âm
                btnRecord.text = "Bắt đầu ghi âm"
            }
        }

        // Sự kiện bấm nút "Phát lại"
        btnPlay.setOnClickListener {
            if (recordings.isNotEmpty()) {
                playRecording(recordings.last()) // Phát lại file mới nhất
            } else {
                Toast.makeText(this, "Không có file ghi âm nào!", Toast.LENGTH_SHORT).show()
            }
        }

        // Xử lý khi chọn một file trong danh sách để phát
        listView.setOnItemClickListener { _, _, position, _ ->
            playRecording(recordings[position])
        }

        // Load danh sách file ghi âm khi ứng dụng khởi động
        loadRecordings()
    }

    // Kiểm tra quyền ghi âm và lưu trữ
    private fun checkPermissions(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Yêu cầu quyền từ người dùng
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            101
        )
    }

    // Bắt đầu ghi âm
    private fun startRecording() {
        fileName = "${externalCacheDir?.absolutePath}/recording_${System.currentTimeMillis()}.3gp"

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC) // Chọn nguồn ghi âm từ micro
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP) // Định dạng file .3gp
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB) // Bộ mã hóa âm thanh
            setOutputFile(fileName) // Đặt vị trí lưu file

            try {
                prepare() // Chuẩn bị ghi âm
                start() // Bắt đầu ghi âm
                Toast.makeText(this@MainActivity, "Bắt đầu ghi âm", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                Log.e("Record", "Lỗi khi ghi âm: ${e.message}")
            }
        }
    }

    // Dừng ghi âm và lưu file vào MediaStore
    private fun stopRecording() {
        mediaRecorder?.apply {
            stop() // Dừng ghi âm
            release() // Giải phóng tài nguyên
        }
        mediaRecorder = null // Đặt lại MediaRecorder về null

        saveRecordingToMediaStore(fileName) // Lưu file vào MediaStore
        Toast.makeText(this, "Ghi âm đã lưu!", Toast.LENGTH_SHORT).show()
    }

    // Lưu file vào MediaStore để hiển thị trong danh sách
    private fun saveRecordingToMediaStore(path: String) {
        val values = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, "Recording_${System.currentTimeMillis()}.3gp")
            put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp")
            put(MediaStore.Audio.Media.DATA, path) // Lưu đường dẫn file
        }

        contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)
        loadRecordings() // Cập nhật danh sách file ghi âm
    }

    // Phát lại file ghi âm từ đường dẫn
    private fun playRecording(path: String) {
        mediaPlayer?.release() // Giải phóng MediaPlayer nếu có
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(path) // Chọn file để phát
                prepare() // Chuẩn bị phát
                start() // Bắt đầu phát
                Toast.makeText(this@MainActivity, "Đang phát: $path", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                Log.e("Play", "Lỗi phát lại: ${e.message}")
            }
        }
    }

    // Truy vấn MediaStore để lấy danh sách file ghi âm đã lưu
    private fun loadRecordings() {
        recordings.clear()

        val projection = arrayOf(MediaStore.Audio.Media.DATA)
        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection, null, null, null
        )

        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            while (it.moveToNext()) {
                recordings.add(it.getString(columnIndex))
            }
        }

        // Hiển thị danh sách file trên ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, recordings)
        findViewById<ListView>(R.id.listView).adapter = adapter
    }

    // Xử lý kết quả khi người dùng cấp quyền
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(this, "Quyền được cấp", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Cần cấp quyền để ghi âm!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
