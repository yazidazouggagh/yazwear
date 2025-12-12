package com.example.yazwear.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.yazwear.Activity.LoginActivity
import com.example.yazwear.service.InactivityService

open class BaseActivity : ComponentActivity() {

    private val inactivityReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == InactivityService.ACTION_USER_INACTIVE) {
                // User is inactive, log them out
                val logoutIntent = Intent(this@BaseActivity, LoginActivity::class.java)
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(logoutIntent)
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(InactivityService.ACTION_USER_INACTIVE)
        LocalBroadcastManager.getInstance(this).registerReceiver(inactivityReceiver, intentFilter)

        resetTimer()
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(inactivityReceiver)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        resetTimer()
    }

    private fun resetTimer() {
        val intent = Intent(this, InactivityService::class.java)
        intent.action = InactivityService.ACTION_RESET_TIMER
        startService(intent)
    }
}