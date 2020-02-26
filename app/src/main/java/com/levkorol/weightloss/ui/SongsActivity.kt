package com.levkorol.weightloss.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.levkorol.weightloss.R
import com.levkorol.weightloss.model.SongInfo
import com.levkorol.weightloss.util.getSongInfo
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.image

// TODO 22.02 #4 =(
class SongsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songs)

        val recyclerView: RecyclerView = findViewById(R.id.my_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
     //   val serializableExtra = intent.getSerializableExtra("as", songInfos)
      //  recyclerView.adapter = Adapter(serializableExtra)

    }

    class Adapter(private var songInfos: List<SongInfo>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        class ViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
            val titleSongTextView: TextView = itemView.findViewById(R.id.titleSongTextView)
           val titleArtistTextView: TextView = itemView.findViewById(R.id.titleArtistTextView)
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

