package com.levkorol.weightloss.ui

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.levkorol.weightloss.R
import kotlinx.android.synthetic.main.activity_login_password.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile_user.*
import org.jetbrains.anko.imageBitmap

class ProfileUserActivity : AppCompatActivity() {

    companion object {
        val TAG = ProfileUserActivity::class.java.simpleName
    }
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { Firebase.firestore }
    private val storage: FirebaseStorage by lazy { Firebase.storage }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_user)
        loadInfo()
    }

    private fun loadInfo() {
        db.collection("users")
            .whereEqualTo("email", auth.currentUser?.email)
            .get()
            .addOnSuccessListener { result ->
                if( result.documents.size ==  0 ) {
                    Toast.makeText(baseContext, "polzovatel ne nayden.", Toast.LENGTH_SHORT).show()
                } else {
                    Log.v(TAG, "loadInfo#succes: ${result.documents[0].get("name")}")
                    updateLayout(result.documents[0])
                  //  updatePhotoProfile()

                }

            }
            .addOnFailureListener { exception ->
                // TODO отображать ошибку
                Log.w(TAG, "Error getting documents.", exception)
            }

    }

//    private fun updatePhotoProfile(){
//        val reference = storage.reference.child("images/${email}")
//        Log.v(TAG,"Image ${reference}")
//        val  ONE_MEGABYTE = 1024 * 1024
//        reference.getBytes(ONE_MEGABYTE.toLong())
//            .addOnSuccessListener {
//            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
//            val handler = object: Handler(Looper.getMainLooper()) {
//                override fun handleMessage(msg: Message) {
//                    photoImageViewP.imageBitmap = bitmap
//                    photoImageViewProfileUser.imageBitmap = bitmap
//                }
//            }
//            handler.sendEmptyMessage(0)
//        }.addOnFailureListener {
//            // TODO отображать ошибку
//        }
//    }

    private fun updateLayout(document: DocumentSnapshot) {
        nameTextView.text = document.get("name").toString()
        countryTextView.text = document.get("country").toString()
        dataTextView.text = document.get("birthday").toString()
        val gender = document.get("gender").toString().toInt()
        if (gender == 1) {
            genderTextView.text = "Man"
        } else {
            genderTextView.text = "Woman"
        }
    }
}
