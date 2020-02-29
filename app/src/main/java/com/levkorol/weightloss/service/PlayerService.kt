package com.levkorol.weightloss.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.storage.StorageVolume
import java.lang.Exception


class PlayerService : IntentService("PlayerService") {

    private val mp: MediaPlayer = MediaPlayer()
    private var uris: List<Uri>? = null
    private var songIndex: Int = -1

    companion object { // зона просто написания писем
        private const val ACTION_PLAY = "ACTION_PLAY"
        private const val ACTION_PAUSE = "ACTION_PAUSE"
        private const val ACTION_VOLUME = "ACTION_VOLUME"
        private const val ACTION_PLAY_NEXT = "ACTION_PLAY_NEXT"
        private const val ACTION_PLAY_PREV = "ACTION_PLAY_PREV"

        private const val EXTRA_VOLUME = "VOLUME"

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

        fun setVolume(context: Context, volume: Float) {
            val intent = Intent(context, PlayerService::class.java)
            intent.action = ACTION_VOLUME
            intent.putExtra(EXTRA_VOLUME, volume)
            context.startService(intent)
        }

        fun playNext(context: Context) {
            val intent = Intent(context, PlayerService::class.java)
            intent.action = ACTION_PLAY_NEXT
            context.startService(intent)
        }

        fun  playPrev(context: Context) {
            val intent = Intent(context, PlayerService::class.java)
            intent.action = ACTION_PLAY_NEXT
            context.startService(intent)
        }

        // TODO 26.02 #1
    }

    override fun onHandleIntent(intent: Intent) { // сама логика
        when (intent.action) {
            ACTION_PLAY -> {
                play(intent)
            }
            ACTION_PAUSE -> {
                pause()
            }
            ACTION_VOLUME -> {
                volume(intent.getFloatExtra(EXTRA_VOLUME, 0.5f))
            }
            ACTION_PLAY_NEXT -> {
               playNext()
            }
            ACTION_PLAY_PREV -> {
                playPrev()
            }
            // TODO 26.02 #1
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

    private fun volume(volume: Float) {
        MediaPlayer().apply {
            setVolume(volume, volume)
            prepareAsync()
        }
    }

    private fun playNext() {
        if (uris != null) {
            songIndex++
            if (songIndex >= uris!!.size) songIndex = 0
            mp.stop()
            mp.setDataSource(this, uris!![songIndex])
            mp.prepareAsync()
            mp.setOnPreparedListener { mp -> mp.start() }
        }
    }

    private fun playPrev() {
        if (songIndex == 0) songIndex++
        if (uris != null) {
            songIndex--
            if (songIndex >= uris!!.size) songIndex.minus(1)
            mp.stop()
            mp.setDataSource(this, uris!![songIndex])
            mp.prepareAsync()
            mp.setOnPreparedListener { mp -> mp.start() }
        }
    }

    // TODO 26.02 #1

//    private fun playU(uris: List<Uri>?) {
//        mp.release()
//        mp = null
//        this.uris = uris
//        if (uris != null) {
//            songIndex = 0
//            play(uris[songIndex])
//        }
//    }
}