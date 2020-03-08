package com.levkorol.weightloss.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.levkorol.weightloss.R

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
    }

    fun signUpWithEmail(v: View) {
        val intent = Intent(this, SignUpWithEmailActivity::class.java)
        startActivity(intent)
    }

    fun signUpWithFacebook(v: View) {

    }
    
    fun signUpWithTwitter(v: View) {

    }
}
