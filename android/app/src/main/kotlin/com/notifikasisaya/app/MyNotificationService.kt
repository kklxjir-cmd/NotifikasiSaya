package com.notifikasisaya.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MyNotificationService : Service() {

    private val CHANNEL_ID = "NotifikasiSayaService"

    override fun onCreate() {
        super.onCreate()
        buatChannelNotifikasi()
        mulaiService()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun buatChannelNotifikasi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Layanan Notifikasi",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Menjaga koneksi dengan server Anda"
            }
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }

    private fun mulaiService() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notifikasi = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("🔔 Notifikasi Saya Aktif")
            .setContentText("Siap mengirim notifikasi ke Telegram")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(1, notifikasi)
    }
}
