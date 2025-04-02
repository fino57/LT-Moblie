package com.example.loadimageapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
class MainActivity : AppCompatActivity() {
    private lateinit var editTextUrl: EditText
    private lateinit var buttonLoad: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextUrl = findViewById(R.id.editTextUrl)
        buttonLoad = findViewById(R.id.buttonLoad)
        progressBar = findViewById(R.id.progressBar)
        imageView = findViewById(R.id.imageView)

        buttonLoad.setOnClickListener {
            val imageUrl = editTextUrl.text.toString().trim()
            if (imageUrl.isNotEmpty()) {
                DownloadImageTask().execute(imageUrl)
            } else {
                Toast.makeText(this, "Vui lòng nhập URL!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private inner class DownloadImageTask : AsyncTask<String, Int, Bitmap?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            progressBar.visibility = View.VISIBLE
            imageView.setImageBitmap(null)
        }

        override fun doInBackground(vararg urls: String?): Bitmap? {
            val urlString = urls[0]
            return try {
                val url = URL(urlString)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input: InputStream = connection.inputStream
                BitmapFactory.decodeStream(input)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            progressBar.visibility = View.GONE
            if (result != null) {
                imageView.setImageBitmap(result)
            } else {
                Toast.makeText(this@MainActivity, "Không thể tải ảnh!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
