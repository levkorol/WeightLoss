package com.levkorol.weightloss.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.levkorol.weightloss.R
import kotlinx.android.synthetic.main.activity_profile_user.*

class ProfileUserActivity : AppCompatActivity() {

    companion object {
        private val TAG = ProfileUserActivity::class.java.simpleName
    }
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { Firebase.firestore }

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
                }

            }
            .addOnFailureListener { exception ->
                // TODO отображать ошибку
                Log.w(TAG, "Error getting documents.", exception)
            }

    }

    private fun updateLayout(document: DocumentSnapshot) {
        nameTextView.text = document.get("name").toString()
        countryTextView.text = document.get("country").toString()
        dataTextView.text = document.get("birthday").toString()

    }
}
