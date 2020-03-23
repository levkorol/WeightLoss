package com.levkorol.weightloss.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.levkorol.weightloss.R
import kotlinx.android.synthetic.main.activity_login_password.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

class LoginPasswordActivity : AppCompatActivity(), TextWatcher {


    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_password)

        //  KeyboardVisibilityEvent.setEventListener(this,this)
    }

//    override fun onVisibilityChanged(isKeyboardOpen: Boolean) {
//      if(isKeyboardOpen) {
//          skroll_login.scrollTo(0,skroll_login.bottom)
//      } else {
//          skroll_login.scrollTo(0,skroll_login.top)
//      }
//    }

    fun goToRegistr(v: View) {
        val intent = Intent(this@LoginPasswordActivity, RegistrationActivity::class.java)
        startActivity(intent)
    }


    fun logIn(v: View) {
        val email = loginEditText.text.toString()
        val password = passwordEditText.text.toString()
        if (validate(email, password)) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    startActivity(Intent(this@LoginPasswordActivity, ProfileActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    showToast("Log in failed.")
                }
        } else {
            showToast("Please enter email and password")
        }
    }


    private fun validate(email: String, password: String) =
        email.isNotEmpty() && password.isNotEmpty()


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        login_button.isEnabled = validate(
            loginEditText.text.toString(),
            passwordEditText.text.toString()
        )
    }
}
