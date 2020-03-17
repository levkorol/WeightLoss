package com.levkorol.weightloss.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.levkorol.weightloss.R
import com.levkorol.weightloss.model.SongInfo
import com.levkorol.weightloss.util.dp
import com.levkorol.weightloss.util.getSongInfo
import kotlinx.android.synthetic.main.activity_main.*


// CTRL + ALT + L
// SHIFT + F6

// 1. переменные: константы и обычные переменные
// 2. методы: public override > public > private

// ProgressBar: progress и max

class MainActivity : AppCompatActivity() {

    companion object {
        private const val MY_PERMISSIONS_REQUEST = 1
        private const val REQUEST_CODE = 2

        private const val SP_PLAYLIST = "PLAYLIST"

        private const val NO_INDEX = -1 // -1 - типа не выбран индекс песни
    }

    private var mp: MediaPlayer? = null
    private var totalTime: Int = 0

    private var originalUris: List<Uri>? = null
    private var uris: List<Uri>? = null
    private var songIndex: Int = NO_INDEX

    var shuffleMode: Boolean = false
    private var repeatMode: Boolean = false

    private val adapter = Adapter(arrayListOf())

    @SuppressLint("HandlerLeak")
    var handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val currentPosition = msg.what

            positionBar.progress = currentPosition

            val elapsedTime = createTimeLabel(currentPosition)
            elapsedTimeLabel.text = elapsedTime

            val remainingTime = createTimeLabel(totalTime - currentPosition)
            remainingTimeLabel.text = "-$remainingTime"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadPreferences()

        val recyclerView: RecyclerView = findViewById(R.id.my_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        volumeBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        val volumeNum = progress / 100.0f
                        mp?.setVolume(volumeNum, volumeNum)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            }
        )

        positionBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        mp?.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            }
        )

        Thread(Runnable {
            while (true) {
                try {
                    if (mp != null) {
                        val msg = Message()
                        msg.what = mp?.currentPosition ?: 0
                        handler.sendMessage(msg)
                    }
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }
            }
        }).start()
    }

    override fun onResume() {
        super.onResume()
        load()
    }

    override fun onPause() {
        super.onPause()
        savePreferences()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    requestAudioFiles()
                } else {
                    Toast.makeText(
                        this, "razreshite prilozheniu chtenie audio s ustroystva",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }

        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    val uris = mutableListOf<Uri>()
                    if (intent?.data != null) {
                        val uri = intent.data
                        uris.add(uri)
                        play(uris)
                    } else {
                        val clipData = intent?.clipData
                        if (clipData != null) {
                            for (i in 0 until clipData.itemCount) {
                                uris.add(clipData.getItemAt(i).uri)
                            }
                        }
                        originalUris = uris
                        play(uris)
                    }
                }

            }
            else -> {

            }
        }
    }

    // CLICK-METHODS

    fun playBtnOnClick(v: View) {
        if (mp?.isPlaying == true) {
            mp?.pause()
        } else {
            mp?.start()
        }
        adapter.notifyDataSetChanged()
        updatePlayButton()
    }

    fun addSongs(v: View) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST
            )
        } else {
            requestAudioFiles()
        }
    }

    fun goToMenu(v: View) {
        val intent = Intent(this@MainActivity, ProfileActivity::class.java)
        val songInfos: ArrayList<SongInfo> = arrayListOf()
        intent.putExtra("as", songInfos)
        startActivity(intent)
    }

    fun listSongs(v: View) {
        if (uris == null) {
            Toast.makeText(
                this, "add your songs",
                Toast.LENGTH_LONG
            ).show()
//            adapter.songInfos = songInfos
//            adapter.notifyDataSetChanged()
        } else {
            var songInfos: MutableList<SongInfo> = arrayListOf()
            for (uri in uris!!) {
                val songs = getSongInfo(this, uri)
                songInfos.add(songs!!)
            }
            adapter.songInfos = songInfos
            adapter.notifyDataSetChanged()
        }

        val handler = Handler()
        handler.postDelayed({
            val height = songsLayout.measuredHeight
            if (songsLayout.translationY == 0f) {
                val h = height.toFloat() - dp(20)
                songsLayout.translationY = -h
            } else {
                songsLayout.translationY = 0f
            }
            weightLossModeLayout.translationY = songsLayout.translationY + dp(50)
        }, 200)
    }

    fun playPrevBtnOnClick(view: View) {
        if (songIndex == 0) songIndex++
        if (uris != null) {
            songIndex--
            if (songIndex >= uris!!.size) songIndex.minus(1)
            mp?.stop()
            play(songIndex)
        }
    }

    fun shufleBtnOnClick(view: View) {
        mp?.stop()
        if (shuffleMode) {
            shuffleImageView.setBackgroundResource(R.drawable.offbtn)
            shuffleMode = false
            play(originalUris)
        } else {
            shuffleImageView.setBackgroundResource(R.drawable.onbtn)
            shuffleMode = true
            play(uris?.shuffled())
        }
    }

    fun repeatBtnOnClick(view: View) {
        if (repeatMode) {
            repeatImageView.setBackgroundResource(R.drawable.offbtn)
            repeatMode = false
        } else {
            repeatImageView.setBackgroundResource(R.drawable.onbtn)
            repeatMode = true
        }
    }

    fun premium(view: View) {
        val intent = Intent(this@MainActivity, PremiumActivity::class.java)
        startActivity(intent)
    }

    fun playNextBtnOnClick(view: View) {
        if (uris != null) {
            songIndex++
            if (songIndex >= uris!!.size) songIndex = 0
            mp?.stop()
            play(songIndex)
        }
    }

    // PRIVATE METHODS

    private fun updatePlayButton() {
        if (mp?.isPlaying == true) {
            playBtn.setBackgroundResource(R.drawable.pause)
        } else {
            playBtn.setBackgroundResource(R.drawable.playbutton)
        }

    }

    private fun load() {
        Log.v("WEIGHT-LOSS", "load: $songIndex")

        val uri = uris?.get(songIndex)
        val songInfo = uri?.let { getSongInfo(this, uri) }
        if (songInfo == null) {
            Toast.makeText(
                this, "ne udalos zagruzit pesn",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Log.v("WEIGHT-LOSS", "play: $songInfo")
            titleSongTextView.text = songInfo.title
            titleArtistTextView.text = songInfo.artist
            if (songInfo.albumBitmap == null) {
                albumImageView.setImageResource(R.drawable.photoalbum)
            } else {
                albumImageView.setImageBitmap(songInfo.albumBitmap)
            }
        }

    }

    private fun play(newSongIndex: Int) {
        try {
            if (uris != null && uris!!.isNotEmpty() && newSongIndex >= 0 && newSongIndex < uris!!.size) {
                this.songIndex = newSongIndex
                adapter.notifyDataSetChanged()
                val uri = uris!![newSongIndex]
                mp = MediaPlayer().apply {
                    setDataSource(applicationContext, uri)
                    //isLooping = true
                    setVolume(0.5f, 0.5f)
                    try {

                        prepareAsync()
                    } catch (e: Exception) {
                    }
                }

                mp?.setOnPreparedListener { mp ->
                    mp.start()
                    updatePlayButton()
                    totalTime = mp.duration
                    positionBar.max = totalTime
                }

                mp?.setOnCompletionListener {
                    if (repeatMode) {
                        mp?.stop()
                        play(newSongIndex)
                    } else {
                        this.songIndex++
                        if (this.songIndex >= uris!!.size) this.songIndex = 0
                        mp?.stop()
                        play(this.songIndex)
                    }

                }
                load()
//                val songInfo = getSongInfo(this, uri)
//                if (songInfo == null) {
//                    Toast.makeText(
//                        this, "ne udalos zagruzit pesn",
//                        Toast.LENGTH_LONG
//                    ).show()
//                } else {
//                    Log.v("WEIGHT-LOSS", "play: $songInfo")
//                    titleSongTextView.text = songInfo.title
//                    titleArtistTextView.text = songInfo.artist
//                    if (songInfo.albumBitmap == null) {
//                        albumImageView.setImageResource(R.drawable.photoalbum)
//                    } else {
//                        albumImageView.setImageBitmap(songInfo.albumBitmap)
//                    }
//                }
            } else {
                return
            }
        } catch (e: Exception) {
        }
    }

    private fun play(uris: List<Uri>?) {
        mp?.release()
        mp = null
        this.uris = uris
        play(0)
    }

    private fun play(uri: Uri) {
        val i = uris?.indexOf(uri) ?: NO_INDEX
        play(i)
    }

    private fun createTimeLabel(time: Int): String {
        val min = time / 1000 / 60
        val sec = time / 1000 % 60
        return "${"%02d".format(min)}:${"%02d".format(sec)}"
    }

    private fun requestAudioFiles() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "audio/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(
            intent,
            REQUEST_CODE
        )
    }

    private fun savePreferences() {
        val sp = this.getSharedPreferences("settings", Context.MODE_PRIVATE)
        if (uris != null) { // List<Uri> -> Set<String>
            val urisAsStrings: List<String> = uris!!.map { uri -> uri.toString() }
            sp.edit()
                .putStringSet(SP_PLAYLIST, urisAsStrings.toSet())
                .putInt("SONGINDEX", songIndex)
                .apply()

        }
    }

    private fun loadPreferences() {
        val sp = this.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val stringsSet = sp.getStringSet(SP_PLAYLIST, setOf()) // Set<String> -> List<Uri>
        uris = stringsSet.map { string -> Uri.parse(string) }.toList()
        songIndex = sp.getInt("SONGINDEX", NO_INDEX)
    }

    private fun getCurrentUri(): Uri? {
        return if (uris == null || songIndex > uris!!.size || songIndex < 0) {
            null
        } else {
            uris!![songIndex]
        }
    }

    inner class Adapter(var songInfos: List<SongInfo>) :
        RecyclerView.Adapter<Adapter.ViewHolder>() {

        inner class ViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
            val titleSongTextView: TextView = itemView.findViewById(R.id.titleSongTextViewInList)
            val titleArtistTextView: TextView =
                itemView.findViewById(R.id.titleArtistTextViewInList)
            val playImageView: ImageView = itemView.findViewById(R.id.playImageView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val songView = LayoutInflater.from(parent.context).inflate(
                R.layout.layout_song,
                parent,
                false
            ) as ViewGroup
            return ViewHolder(songView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val songInfo = songInfos[position]
            holder.titleSongTextView.text = songInfo.title
            holder.titleArtistTextView.text = songInfo.artist
            holder.playImageView.setOnClickListener {


                if (getCurrentUri() == songInfo.uri && mp?.isPlaying == true) {
                    holder.playImageView.setImageResource(R.drawable.pause)
                } else {
                    holder.playImageView.setImageResource(R.drawable.playbutton)
                }

                //    mp?.stop()

                play(songInfo.uri)


            }
            if (getCurrentUri() == songInfo.uri && mp?.isPlaying == true) {
                holder.playImageView.setImageResource(R.drawable.pause)
            } else {
                holder.playImageView.setImageResource(R.drawable.playbutton)
            }
        }

        override fun getItemCount() = songInfos.size
    }

}


