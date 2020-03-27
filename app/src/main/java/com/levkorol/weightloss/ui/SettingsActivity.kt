package com.levkorol.weightloss.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.levkorol.weightloss.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN


                    if (auth.currentUser != null) swich.isEnabled



        swich.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you want logout?")
            builder.setPositiveButton("YES"){dialog, which ->
                FirebaseAuth.getInstance().signOut()
            }


            builder.setNegativeButton("No"){dialog,which ->

            }


            val dialog: AlertDialog = builder.create()

            dialog.show()
        }


//
//        // show simple dialog with text
//        AlertDialog.Builder(this)
//            .setMessage("viyty iz akka")
//            .setPositiveButton("Yes", null)
//            .setNegativeButton("No", null)
//            .show()





    }


//    fun logOut(v: View) {
//        FirebaseAuth.getInstance().signOut()
//    }

    fun back(view: View) {
        finish()
    }

    fun goToMenu(v: View) {
        val intent = Intent(this, ProfileActivity::class.java)

        startActivity(intent)
    }

    fun clickLaungage(v: View) {
        startActivity(Intent(this, Language::class.java))
    }


}
