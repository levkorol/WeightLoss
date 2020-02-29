package com.levkorol.weightloss.model

import android.graphics.Bitmap
import android.net.Uri

data class SongInfo(
    val uri: Uri,
    val title: String?,
    val artist: String?,
    val albumBitmap: Bitmap? // картинка постера
)