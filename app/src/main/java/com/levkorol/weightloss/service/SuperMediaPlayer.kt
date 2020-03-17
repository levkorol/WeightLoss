package com.levkorol.weightloss.service

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import java.io.IOException

class SuperMediaPlayer : MediaPlayer() {

    private var dataSource: Uri? = null

    override fun setDataSource(context: Context, uri: Uri) {
        dataSource = uri
        super.setDataSource(context, uri)
    }

    fun getDataSource(): Uri? {
        return dataSource
    }

}