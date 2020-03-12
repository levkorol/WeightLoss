package com.levkorol.weightloss.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.levkorol.weightloss.R

class ProfileUserActivity : AppCompatActivity() {

    companion object {
        private val TAG = ProfileUserActivity::class.java.simpleName
    }

    private val db: FirebaseFirestore by lazy { Firebase.firestore }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_user)
        loadInfo()
    }

    private fun loadInfo() {
        db.collection("users")
            .whereEqualTo("email", "test1@gmail.com") // TODO актуальный емейл
            .get()
            .addOnSuccessListener { result ->
                // TODO если result.size == 0 то ошибку что пользователь не найден((
                Log.v(TAG, "loadInfo#succes: ${result.documents[0].get("weight")}")
                updateLayout(result.documents[0])
            }
            .addOnFailureListener { exception ->
                // TODO отображать ошибку
                Log.w(TAG, "Error getting documents.", exception)
            }

    }

    private fun updateLayout(document: DocumentSnapshot) {
        // TODO заполнить все поля нужными данными
    }
}
