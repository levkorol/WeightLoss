package com.levkorol.weightloss.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.levkorol.weightloss.R
import kotlinx.android.synthetic.main.activity_login_password.*

class LoginPasswordActivity : AppCompatActivity() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_password)
    }

    fun goToRegistr(v: View){
        val intent = Intent(this@LoginPasswordActivity, RegistrationActivity::class.java)
        startActivity(intent)
    }
    fun logIn (v:View){
        auth.signInWithEmailAndPassword(loginEditText.text.toString(),
            passwordEditText.text.toString()
        ).addOnSuccessListener {

            val intent = Intent(this@LoginPasswordActivity, ProfileActivity::class.java)
            startActivity(intent)
        }.addOnFailureListener {
            Toast.makeText(baseContext, "Log in failed.", Toast.LENGTH_SHORT).show()
        }
    }

}
