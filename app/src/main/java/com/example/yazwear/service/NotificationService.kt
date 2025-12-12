package com.example.yazwear.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.yazwear.util.NotificationHelper

class NotificationService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationHelper = NotificationHelper(this)
        notificationHelper.showNotification("Yazwear Service", "Ceci est une notification de notre service.")
                stopSelf()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}