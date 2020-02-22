package com.levkorol.weightloss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.levkorol.weightloss.R
import com.levkorol.weightloss.model.SongInfo

// TODO 22.02 #4
class SongsActivity : AppCompatActivity() {

    class Adapter(private var songInfos: List<SongInfo>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        class ViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
            val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val songView = LayoutInflater.from(parent.context).inflate(R.layout.layout_song, parent, false) as ViewGroup
            return ViewHolder(songView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val songInfo = songInfos[position]
            holder.titleTextView.text = songInfo.title
        }

        override fun getItemCount() = songInfos.size
    }

}

