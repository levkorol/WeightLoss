package com.levkorol.weightloss.util

import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.METADATA_KEY_ARTIST
import android.media.MediaMetadataRetriever.METADATA_KEY_TITLE
import android.net.Uri
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import com.levkorol.weightloss.model.SongInfo

fun getSongInfo(context: Context, songUri: Uri): SongInfo? {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(context, songUri)
    return SongInfo(
        title = retriever.extractMetadata(METADATA_KEY_TITLE),
        artist = retriever.extractMetadata(METADATA_KEY_ARTIST),
        albumBitmap = toBitmap(retriever.embeddedPicture)
    )
}

fun toBitmap(bytes: ByteArray?): Bitmap? {
    return if (bytes == null) {
        null
    } else {
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}

fun dp(dp: Int) = Math.ceil((dp * density()).toDouble()).toInt()

fun density() = getDisplayMetrics().density

fun getDisplayMetrics(): DisplayMetrics = Resources.getSystem().displayMetrics