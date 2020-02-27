package com.levkorol.weightloss.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.levkorol.weightloss.R
import com.levkorol.weightloss.model.SongInfo
import com.levkorol.weightloss.util.getSongInfo

// TODO 22.02 #4 =(
class SongsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songs)

        @Suppress("UNCHECKED_CAST")
        val uris = intent.getSerializableExtra("as") as ArrayList<Uri>
        val songInfos: ArrayList<SongInfo> = arrayListOf()
        for (uri in uris) {
            val songs = getSongInfo(this, uri)
            songInfos.add(songs!!)
        }

        val recyclerView: RecyclerView = findViewById(R.id.my_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = Adapter(songInfos)
    }

    class Adapter(private var songInfos: List<SongInfo>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        class ViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
            val titleSongTextView: TextView = itemView.findViewById(R.id.titleSongTextViewInList)
            val titleArtistTextView: TextView = itemView.findViewById(R.id.titleArtistTextViewInList)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val songView = LayoutInflater.from(parent.context).inflate(R.layout.layout_song, parent, false) as ViewGroup
            return ViewHolder(songView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val songInfo = songInfos[position]
            holder.titleSongTextView.text = songInfo.title
           holder.titleArtistTextView.text = songInfo.artist

        }

        override fun getItemCount() = songInfos.size
    }

}

