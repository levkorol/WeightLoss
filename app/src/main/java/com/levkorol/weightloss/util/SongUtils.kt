package com.levkorol.weightloss.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.levkorol.weightloss.model.SongInfo

fun getSongInfo(context: Context, songUri: Uri): SongInfo? {
    Log.v("WEIGHT-LOSS", songUri.toString())
    val id = songUri.path?.split(":")?.get(1)
    val cursor: Cursor? = context.contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, // адрес базы данных
        arrayOf(MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM_ID), // TODO 22.02 #3
        MediaStore.Audio.Media._ID + "= ?",
        arrayOf(id.toString()),
        null
    )
    try {
        return if (cursor == null || cursor.count == 0) {
            null
        } else {
            cursor.moveToFirst()
            SongInfo(cursor.getString(0), cursor.getInt(1)) // TODO 22.02 #3
        }
    } finally {
        cursor?.close()
    }
}