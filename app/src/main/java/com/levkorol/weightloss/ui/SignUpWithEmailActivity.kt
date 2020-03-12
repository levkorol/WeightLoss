package com.levkorol.weightloss.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.levkorol.weightloss.R
import kotlinx.android.synthetic.main.activity_sign_up_with_email.*


class SignUpWithEmailActivity : AppCompatActivity() {

    companion object {
        private val TAG = SignUpWithEmailActivity::class.java.simpleName
    }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { Firebase.firestore }
//    private val storage: FirebaseStorage by lazy { Firebase.storage }

//    private val imageUri: Uri? = null
//    private var firebaseImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_with_email)

        signUp.setOnClickListener {
            createUser()
        }
    }

    private fun createUser() {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // получение базовой информации о пользователе:
                    val user = auth.currentUser
                    Log.d(TAG, "createUserWithEmail:success: ${user?.displayName}, ${user?.email}")
//                    if (imageUri != null) uploadPhoto(imageUri) // TODO сначала загружаем фото пользователя
                    saveInfo() // TODO сохранить в Firestore дополнительную инофрмацию о пользователе
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    // TODO отображать подробную инфу об ошибке?
                }
            }
    }

    private fun saveInfo() {
        // TODO поменять все поля на актуальные
        val user = hashMapOf(
            "first" to "Ada",
            "last" to "Lovelace",
            "born" to 1815
//            "photo" to firebaseImage
        )

        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                // TODO 2. переход к профилю
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                // TODO отображать ошибку?
            }
    }

//    private fun uploadPhoto(photoUri: Uri) {
//        val reference = storage.reference.child("images/${photoUri.lastPathSegment}") // "images/test1@gmail.com.jpg"
//        val uploadTask = reference.putFile(photoUri)
//        uploadTask.addOnFailureListener {
//            // TODO отобразить ошибку
//        }.addOnSuccessListener {
//            firebaseImage = "images/${photoUri.lastPathSegment}"
//        }
//    }

}

