package com.levkorol.weightloss.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.levkorol.weightloss.R

class LoginPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_password)
    }

    fun goToRegistr(v: View){
        val intent = Intent(this@LoginPasswordActivity, RegistrationActivity::class.java)
        startActivity(intent)
    }
}
