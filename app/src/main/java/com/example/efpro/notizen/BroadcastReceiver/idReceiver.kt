package com.example.efpro.notizen.BroadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat.*
import com.example.efpro.notizen.Activities.LoginActivity
import com.example.efpro.notizen.Activities.navigate

class idReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        when {
            intent?.action == Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    val message = intent.getStringExtra(Intent.EXTRA_TEXT)
                    val intento = Intent(context, navigate::class.java)//Redirigimos a contactos
                    intento.putExtra("content",message)
                    startForegroundService(context,intento)
                }
            }
        }
    }
}
