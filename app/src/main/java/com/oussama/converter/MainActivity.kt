package com.oussama.converter

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.arthenica.ffmpegkit.FFmpegKit
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var percentText: TextView
    private var inputUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )

        val pickBtn = findViewById<Button>(R.id.pickBtn)
        val convertBtn = findViewById<Button>(R.id.convertBtn)
        progressBar = findViewById(R.id.progressBar)
        percentText = findViewById(R.id.percentText)

        pickBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            startActivityForResult(intent, 100)
        }

        convertBtn.setOnClickListener {
            inputUri?.let {
                convertVideo(it)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            inputUri = data?.data
            Toast.makeText(this, "File selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun convertVideo(uri: Uri) {
        val inputPath = FileUtils.getPath(this, uri) ?: return

        val outDir = File("/storage/emulated/0/Download/outmp4")
        if (!outDir.exists()) outDir.mkdirs()

        val outputPath = outDir.absolutePath + "/output.mp4"

        val cmd = "-i $inputPath -c:v libx264 -preset fast -crf 23 -tune fastdecode -c:a aac -b:a 128k $outputPath"

        FFmpegKit.executeAsync(cmd) {
            runOnUiThread {
                progressBar.progress = 100
                percentText.text = "100%"
                Toast.makeText(this, "Done", Toast.LENGTH_LONG).show()
            }
        }
    }
}
