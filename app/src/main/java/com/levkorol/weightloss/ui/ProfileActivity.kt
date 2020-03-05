package com.levkorol.weightloss.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.levkorol.weightloss.R
import com.levkorol.weightloss.model.SongInfo

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }

    fun goToMenu(v: View) {
        val intent = Intent(this, LoginPasswordActivity::class.java)
//        val songInfos: ArrayList<SongInfo> = arrayListOf()
//        intent.putExtra("as", songInfos)
        startActivity(intent)
    }
}
