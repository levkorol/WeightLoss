package com.levkorol.weightloss.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.net.Uri


class PlayerService : IntentService("PlayerService") {

    companion object {
        private const val ACTION_PLAY = "ACTION_PLAY"
        private const val ACTION_STOP = "ACTION_STOP"

        fun play(context: Context, uri: Uri) {
            val intent = Intent(context, PlayerService::class.java)
            intent.action = ACTION_PLAY
            intent.data = uri
            context.startService(intent)
        }
    }

    override fun onHandleIntent(intent: Intent) {
        when(intent.action) {
            ACTION_PLAY -> {
                // TODO
            }
            ACTION_STOP -> {
                // TODO
            }
        }
    }

}