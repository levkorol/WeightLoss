package com.levkorol.weightloss.ui

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.levkorol.weightloss.R
import com.levkorol.weightloss.data.UserRepository
import com.levkorol.weightloss.model.SongInfo
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile_user.*
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.imageBitmap
import java.util.logging.LogManager

class ProfileActivity : AppCompatActivity() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { Firebase.firestore }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initializeNavigation()

        loadInfo()
    }

    fun goToPleer(v: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun initializeNavigation() {
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profileItem -> {
                    if(auth.currentUser == null) {
                        startActivity(Intent(this, LoginPasswordActivity::class.java))
                    } else {
                        val profileIntent = Intent(this, ProfileUserActivity::class.java)
                        profileIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                        startActivity(profileIntent)
                    }

                }

                R.id.pleerItem -> {
                    val pleerIntent = Intent(this, MainActivity::class.java)
                    pleerIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    startActivity(pleerIntent)
                }
                R.id.premiumItem -> {
                    val profileIntent = Intent(this, PremiumActivity::class.java)
                    profileIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    startActivity(profileIntent)
                }
                R.id.inviteFriendsItem -> {
                    val profileIntent = Intent(this, InvateFriendsActivity::class.java)
                    profileIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    startActivity(profileIntent)
                }
                R.id.instructionsItem -> {
//                    val profileIntent = Intent(this, RegistrationActivity::class.java)
//                    profileIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
//                    startActivity(profileIntent)
                }
                R.id.otherAppsItem-> {
//                    val profileIntent = Intent(this, RegistrationActivity::class.java)
//                    profileIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
//                    startActivity(profileIntent)
                }
                R.id.settingsItem -> {
                    if(auth.currentUser == null) {
                        showToast("Please log in.")
                    } else {
                        val settingsIntent = Intent(this, SettingsActivity::class.java)
                        settingsIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                        startActivity(settingsIntent)
                    }

                }
                else -> {
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun loadInfo() {
        db.collection("users")
            .whereEqualTo("email", auth.currentUser?.email)
            .get()
            .addOnSuccessListener { result ->
                if (result.documents.size ==  0 ) {
                    Toast.makeText(baseContext, "polzovatel ne nayden.", Toast.LENGTH_SHORT).show()
                } else {
                    Log.v(ProfileUserActivity.TAG, "loadInfo#succes: ${result.documents[0].get("name")}")
                    updateLayout(result.documents[0])
                    UserRepository.updatePhotoProfile(photoImageViewP)
                }

            }
            .addOnFailureListener { exception ->
                // TODO отображать ошибку
                Log.w(ProfileUserActivity.TAG, "Error getting documents.", exception)
            }

    }


    private fun updateLayout(document: DocumentSnapshot) {
        name_user.text = document.get("name").toString()

    }
}



