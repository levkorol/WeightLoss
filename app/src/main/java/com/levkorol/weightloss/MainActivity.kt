package com.levkorol.weightloss

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.EXTRA_ALLOW_MULTIPLE
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var mp: MediaPlayer? = null
    private var totalTime: Int = 0
    private val MY_PERMISSIONS_REQUEST = 1
    private val reqCode = 2

    private var uris: List<Uri>? = null
    private var currentI: Int = -1

    fun play(uris: List<Uri>?) {
        if (uris != null) {
            this.uris = uris
            currentI = 0
            play()
        }
    }

    fun playNext() {
        if (uris != null) {
            currentI++
            if (currentI >= uris!!.size) currentI = 0
            play()
        }
    }

    fun play() {
        mp?.release()
        mp = null
        if (uris != null) {
            Log.v("GUB", "play: $currentI, ${uris!![currentI]}")
            mp = MediaPlayer().apply {
                setDataSource(applicationContext, uris!![currentI])
                isLooping = true
                setVolume(0.5f, 0.5f)
                //            totalTime = duration
                try {
                    prepareAsync()

                } catch (e: Exception) {

                }
            }

            mp?.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
                override fun onPrepared(mp: MediaPlayer?) {
                    mp?.start()
                    totalTime = mp!!.duration
                    positionBar.max = totalTime
                }

            })
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Volume Bar
        volumeBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        var volumeNum = progress / 100.0f
                        mp?.setVolume(volumeNum, volumeNum)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            }
        )

        //PositionBar
        positionBar.max = totalTime

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

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            }
        )

        //Thread
        Thread(Runnable {
            while (true) {
                if (mp != null) {
                    try {
                        var msg = Message()
                        msg.what = mp?.currentPosition ?: 0
                        handler.sendMessage(msg)
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {
                    }
                }
            }
        }).start()

        nextSong.setOnClickListener {
            playNext()
        }
    }


    @SuppressLint("HandlerLeak")
    var handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            var currentPosition = msg.what

            //Update positionBar
            positionBar.progress = currentPosition

            //Update Labeles
            var elapsedTime = createTimeLabel(currentPosition)
            elapsedTimeLabel.text = elapsedTime

            var remainingTime = createTimeLabel(totalTime - currentPosition)
            remainingTimeLabel.text = "-$remainingTime"
        }
    }

    fun createTimeLabel(time: Int): String {
        var timeLabel = ""
        val min = time / 1000 / 60
        val sec = time / 1000 % 60

        timeLabel = "${"%02d".format(min)}:${"%02d".format(sec)}"

        return timeLabel
    }


    fun playBtnOnClick(v: View) {

        if (mp?.isPlaying == true) {
            //Stop
            mp?.pause()
            playBtn.setBackgroundResource(R.drawable.playbutton)
        } else {
            //Start
            mp?.start()
            playBtn.setBackgroundResource(R.drawable.pause)
        }
    }

    fun btnListAudio(v: View) {
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

    //zaprashivaet razr
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
                    Toast.makeText(this, " gfgdf", Toast.LENGTH_LONG).show()

                }
                return
            }


            else -> {
                // Ignore all other requests.
            }
        }
    }

    fun requestAudioFiles() {
        //  val intent = Intent()
        //   intent.type = "audio/*"
        //  intent.action = Intent.ACTION_GET_CONTENT
        //  startActivityForResult(Intent.createChooser(intent, "Select Audio "), reqCode)
        //  val REQUEST_CODE = 1001

        val audioIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        audioIntent.addCategory(Intent.CATEGORY_OPENABLE)
        audioIntent.putExtra(EXTRA_ALLOW_MULTIPLE, true)
        audioIntent.type = "audio/*"

//        val audioIntent = Intent(
//            Intent.ACTION_PICK,
//            android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//        ).apply {
//            type = "video/*"
//            putExtra(EXTRA_ALLOW_MULTIPLE, true)
//        }
        startActivityForResult(audioIntent, reqCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            reqCode -> {
                if (resultCode == RESULT_OK) {
                val clipData = data?.clipData
                val uris: MutableList<Uri> = mutableListOf()
                    if (clipData != null) {
                        for (i in 0 until clipData.itemCount) {
                            uris.add(clipData.getItemAt(i).uri)
                        }
                        play(uris)
                    }
                }
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    fun goToMenu(v: View) {
        val intent = Intent(this@MainActivity, LoginPasswordActivity::class.java)
        startActivity(intent)
    }


}


