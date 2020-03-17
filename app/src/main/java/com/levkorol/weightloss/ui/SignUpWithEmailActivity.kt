package com.levkorol.weightloss.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.levkorol.weightloss.R
import kotlinx.android.synthetic.main.activity_sign_up_with_email.*


class SignUpWithEmailActivity : AppCompatActivity() {

    companion object {
        private val TAG = SignUpWithEmailActivity::class.java.simpleName
    }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { Firebase.firestore }
    private val storage: FirebaseStorage by lazy { Firebase.storage }
    private val imageUri: Uri? = null
    private var firebaseImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_with_email)

        signUp.setOnClickListener {
            createUser()
        }

        inputName.error = "ОШИБОЧКА(((("
        editTextName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // TODO
            }
        })
    }

    private fun createUser() {

        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail>success: ${auth.currentUser?.email}")
                    if (imageUri != null) uploadPhoto(imageUri) // TODO загружаем фото (если есть) в хранилище
                    saveInfo() // TODO сохраняем дополнительную информацию в базу данных
                } else {
                    Log.w(TAG, "createUserWithEmail>failure", task.exception)

                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    // TODO подробная информация об ошибке

                }
            }
    }

    private fun saveInfo() {
//        val genderMan = radio_man
//        val genderWoman = radio_woman
//        radio_man.isChecked
//        radoigroup.checkedRadioButtonId
        val name = editTextName.text.toString()
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        val country = editTextCountry.text.toString()
        val dataOfBirthd = editTextData.text.toString()
        val user = hashMapOf(
           // "GenderMan"  to genderMan,
            "name" to name,
            "email" to email,
            "password" to password,
            "country" to country,
            "birthday" to dataOfBirthd
//            "photo" to firebaseImage
        )

        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "saveInfo>success: ${documentReference.id}")
                val profileIntent = Intent(this, ProfileUserActivity::class.java)
                profileIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(profileIntent)

            }
            .addOnFailureListener { e ->
                Log.w(TAG, "saveInfo>failure", e)
                // TODO подробная информация об ошибке
            }
    }

    private fun uploadPhoto(photoUri: Uri) {
        val reference = storage.reference.child("images/${photoUri.lastPathSegment}") // "images/test1@gmail.com.jpg"
        val uploadTask = reference.putFile(photoUri)
        uploadTask.addOnFailureListener {
            // TODO подробная информация об ошибке
        }.addOnSuccessListener {
            firebaseImage = "images/${photoUri.lastPathSegment}"
        }
    }

}

