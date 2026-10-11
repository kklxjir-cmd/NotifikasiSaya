package com.notifikasisaya.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var serverInput: EditText
    private lateinit var simpanBtn: Button
    private lateinit var kirimBtn: Button

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "✅ Izin notifikasi diberikan!", Toast.LENGTH_SHORT).show()
                startService(Intent(this, MyNotificationService::class.java))
            } else {
                Toast.makeText(this, "⚠️ Izin ditolak", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serverInput = findViewById(R.id.serverUrl)
        simpanBtn = findViewById(R.id.simpanBtn)
        kirimBtn = findViewById(R.id.kirimBtn)

        val prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        serverInput.setText(prefs.getString("server_url", ""))

        simpanBtn.setOnClickListener {
            prefs.edit().putString("server_url", serverInput.text.toString()).apply()
            Toast.makeText(this, "URL Disimpan!", Toast.LENGTH_SHORT).show()
        }

        kirimBtn.setOnClickListener { kirimNotifikasi() }

        mintaIzinNotifikasi()
    }

    private fun mintaIzinNotifikasi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                startService(Intent(this, MyNotificationService::class.java))
            }
        }
    }

    private fun kirimNotifikasi() {
        val url = serverInput.text.toString().trim()
        if (url.isEmpty()) {
            Toast.makeText(this, "Isi URL server dulu!", Toast.LENGTH_SHORT).show()
            return
        }

        Thread {
            try {
                val json = JSONObject().apply {
                    put("namaPerangkat", Build.MODEL)
                    put("info", "Halo dari perangkat! Waktu: ${System.currentTimeMillis()}")
                }.toString()

                val body = json.toRequestBody("application/json".toMediaType())
                val request = Request.Builder().url(url).post(body).build()
                val response = OkHttpClient().newCall(request).execute()

                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this, "✅ Terkirim ke Telegram!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "❌ Gagal: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}
