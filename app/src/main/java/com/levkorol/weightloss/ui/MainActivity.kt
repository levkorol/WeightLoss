package com.levkorol.weightloss.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.levkorol.weightloss.R
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

// CTRL + ALT + L
// SHIFT + F6

// 1. переменные: константы и обычные переменные
// 2. методы: public override > public > private

// ProgressBar: progress и max

class MainActivity : AppCompatActivity() {

    companion object {
        private const val MY_PERMISSIONS_REQUEST = 1
        private const val REQUEST_CODE = 2
    }

    private var mp: MediaPlayer? = null
    private var totalTime: Int = 0

    private var uris: List<Uri>? = null
    private var songIndex: Int = -1

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

        Thread(Runnable {
            while (true) {
                try {
                    if (mp != null) {
                        val msg = Message()
                        msg.what = mp?.currentPosition ?: 0
                        handler.sendMessage(msg)
                    }
                    Thread.sleep(1000)
                } catch (e: InterruptedException) { }
            }
        }).start()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    val uris = mutableListOf<Uri>()
                    val clipData = intent?.clipData
                    if (clipData != null) {
                        for (i in 0 until clipData.itemCount) {
                            uris.add(clipData.getItemAt(i).uri)
                        }
                    }
                    play(uris)
                }
            }
            else -> {
                // Ignore all other requests.
            }
        }
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

    fun goToMenu(v: View) {
        val intent = Intent(this@MainActivity, LoginPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun play(uris: List<Uri>?) {
        mp?.release()
        mp = null
        this.uris = uris
        if (uris != null) {
            songIndex = 0
            play(uris[songIndex])
        }
    }

    // TODO добавить playPrev
    // TODO подключить к кнопке
    private fun playNext() {
        if (uris != null) {
            songIndex++
            if (songIndex >= uris!!.size) songIndex = 0
            play(uris!![songIndex])
        }
    }

    private fun play(uri: Uri) {
        mp = MediaPlayer().apply {
            setDataSource(applicationContext, uri)
            isLooping = true
            setVolume(0.5f, 0.5f)
            try {
                prepareAsync()
            } catch (e: Exception) {}
        }

        mp?.setOnPreparedListener { mp ->
            mp.start()
            totalTime = mp.duration
            positionBar.max = totalTime
        }
    }

    private fun createTimeLabel(time: Int): String {
        val min = time / 1000 / 60
        val sec = time / 1000 % 60
        return "${"%02d".format(min)}:${"%02d".format(sec)}"
    }

    private fun requestAudioFiles() {
        //  val intent = Intent()
        //   intent.type = "audio/*"
        //  intent.action = Intent.ACTION_GET_CONTENT
        //  startActivityForResult(Intent.createChooser(intent, "Select Audio "), reqCode)
        //  val REQUEST_CODE = 1001

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "audio/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent,
            REQUEST_CODE
        )
    }


}


