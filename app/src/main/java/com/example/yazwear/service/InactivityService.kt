package com.example.yazwear.service



import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class InactivityService : Service() {

    private val INACTIVITY_TIMEOUT_MS = 30 * 1000L // 30 seconds

    private var timer: CountDownTimer? = null

    companion object {

        const val ACTION_START_TIMER = "com.example.yazwear.ACTION_START_TIMER"
        const val ACTION_RESET_TIMER = "com.example.yazwear.ACTION_RESET_TIMER"
        const val ACTION_STOP_TIMER = "com.example.yazwear.ACTION_STOP_TIMER"


        const val ACTION_USER_INACTIVE = "com.example.yazwear.ACTION_USER_INACTIVE"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_TIMER -> {
                Log.d("InactivityService", "Démarrage du minuteur d'inactivité.")
                startTimer()
            }
            ACTION_RESET_TIMER -> {
                Log.d("InactivityService", "Réinitialisation du minuteur.")
                resetTimer()
            }
            ACTION_STOP_TIMER -> {
                Log.d("InactivityService", "Arrêt du minuteur et du service.")
                stopTimer()
                stopSelf() // Arrête le service
            }
        }

        return START_NOT_STICKY
    }

    private fun startTimer() {
        if (timer != null) return

        timer = object : CountDownTimer(INACTIVITY_TIMEOUT_MS, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                Log.d("InactivityService", "Timeout d'inactivité atteint. Déconnexion.")

                val intent = Intent(ACTION_USER_INACTIVE)
                LocalBroadcastManager.getInstance(this@InactivityService).sendBroadcast(intent)
                stopSelf()
            }
        }.start()
    }

    private fun resetTimer() {
        stopTimer()
        startTimer()
    }

    private fun stopTimer() {
        timer?.cancel()
        timer = null
    }

    override fun onDestroy() {
        stopTimer()
        super.onDestroy()
        Log.d("InactivityService", "Service détruit.")
    }

    override fun onBind(intent: Intent?): IBinder? {

        return null
    }
}
