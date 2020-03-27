package com.levkorol.weightloss.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.levkorol.weightloss.R
import kotlinx.android.synthetic.main.activity_premium.*

class PremiumActivity : AppCompatActivity() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_premium)

        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN


        best.setOnClickListener {
            if (auth.currentUser == null) {
                showToast("Please Login")
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Are you want premium acc?")
                builder.setPositiveButton("YES") { dialog, which -> }
                builder.setNegativeButton("No") { _, _ -> }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }
    }

    fun back(view: View) { finish() }

    fun btn_premium_click(view: View) {}


}
