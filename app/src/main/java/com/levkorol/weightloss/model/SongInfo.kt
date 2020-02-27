package com.levkorol.weightloss.model

import android.graphics.Bitmap

data class SongInfo(
    val title: String?,
    val artist: String?,
    val albumBitmap: Bitmap? // картинка постера
)