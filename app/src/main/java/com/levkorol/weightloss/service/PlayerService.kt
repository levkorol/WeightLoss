package com.levkorol.weightloss.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import java.lang.Exception


class PlayerService : IntentService("PlayerService") {

    private val mp: MediaPlayer = MediaPlayer()

    companion object { // зона просто написания писем
        private const val ACTION_PLAY = "ACTION_PLAY"
        private const val ACTION_PAUSE = "ACTION_PAUSE"
        // TODO 17.02 #2.1 константы для операций

        fun play(context: Context, uri: Uri) { // это типа статический метод
            val intent = Intent(context, PlayerService::class.java)
            intent.action = ACTION_PLAY
            intent.data = uri
            context.startService(intent)
        }

        fun pause(context: Context) { // это типа статический метод
            val intent = Intent(context, PlayerService::class.java)
            intent.action = ACTION_PAUSE
            context.startService(intent)
        }

        // TODO 17.02 #2.2 статический метод setVolume
    }

    override fun onHandleIntent(intent: Intent) { // сама логика
        when(intent.action) {
            ACTION_PLAY -> {
                play(intent)
            }
            ACTION_PAUSE -> {
                pause()
            }
            // TODO 17.02 #2.3 ACTION_VOLUME ->
        }
    }

    private fun play(intent: Intent) {
        mp.setDataSource(applicationContext, intent.data!!)
        mp.setVolume(0.5f, 0.5f)
        mp.prepareAsync()
        mp.setOnPreparedListener { mp -> mp.start() }
    }

    private fun pause() {
        mp.pause()
    }

    // TODO 17.02 #2.4 setVolume (сама логика)

}