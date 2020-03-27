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
import kotlinx.android.synthetic.main.activity_settings.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

class LoginPasswordActivity : AppCompatActivity(), TextWatcher {


    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_password)

        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

    }


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
            showToast(getString(R.string.login_pass))
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
