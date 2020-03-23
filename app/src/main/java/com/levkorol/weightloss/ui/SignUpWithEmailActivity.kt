package com.levkorol.weightloss.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_sign_up_with_email.*
import android.provider.MediaStore
import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.levkorol.weightloss.R
import kotlinx.android.synthetic.main.activity_login_password.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile_user.*
import org.jetbrains.anko.imageBitmap


class SignUpWithEmailActivity : AppCompatActivity() {

    companion object {
        private val TAG = SignUpWithEmailActivity::class.java.simpleName

        private const val PICK_IMAGE = 100
    }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { Firebase.firestore }
    private val storage: FirebaseStorage by lazy { Firebase.storage }
    private var imageUri: Uri? = null
    private var firebaseImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.levkorol.weightloss.R.layout.activity_sign_up_with_email)

        //click
        signUp.setOnClickListener {
            createUser()
        }


        editTextEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (editTextPassword.text!!.length < 6) {
                    editTextPassword.error = "6 characters minimum"
                }
                if (editTextEmail.getText()!!.length < 6 || !editTextEmail.getText().toString().contains(
                        "@"
                    )
                ) {
                    editTextEmail.error = "Enter email"
                }
            }
        })
    }

    fun addPhoto(v: View) {
        openGallery()
    }

    private fun createUser() {

        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        val name = editTextName.text.toString()


        if (validate(email, password, name)) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail>success: ${auth.currentUser?.email}")
                        if (imageUri != null) {
                            uploadPhotoAndSaveInfo(imageUri!!, email)
                        } else {
                            updatePhotoProfile()
                            saveInfo()

                        }
                    } else {
                        Log.w(TAG, "createUserWithEmail>failure", task.exception)
                        showToast("Authentication failed.")
                        // TODO подробная информация об ошибке
                    }
                }
        } else {
            showToast("Please fill in the required fields marked: * !")
        }
    }

    private fun saveInfo() {

        val gender = if (radio_man.isChecked) { 1 } else { 0 }
        val name = editTextName.text.toString()
        val email = editTextEmail.text.toString()
        val country = editTextCountry.text.toString()
        val dataOfBirthd = editTextData.text.toString()
        val user = hashMapOf(

            "gender" to gender,
            "name" to name,
            "email" to email,
            "country" to country,
            "birthday" to dataOfBirthd,
            "photo" to firebaseImage
        )

        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "saveInfo>success: ${documentReference.id}")
                startActivity(Intent(this, ProfileUserActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "saveInfo>failure", e)
                showToast("Something wrong happened. Please try again later")
            }
    }

    private fun uploadPhotoAndSaveInfo(photoUri: Uri, email: String) {
        val reference = storage.reference.child("images/${email}")
        val uploadTask = reference.putFile(photoUri)
        uploadTask.addOnFailureListener {
            // TODO подробная информация об ошибке
        }.addOnSuccessListener {
            reference.downloadUrl.addOnSuccessListener {
                firebaseImage = it.toString()
                saveInfo()
            }
        }
    }

    private fun updatePhotoProfile(){
        val reference = storage.reference.child("images/${email}")
        Log.v(ProfileUserActivity.TAG,"Image ${reference}")
        val  ONE_MEGABYTE = 1024 * 1024
        reference.getBytes(ONE_MEGABYTE.toLong())
            .addOnSuccessListener {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                val handler = object: Handler(Looper.getMainLooper()) {
                    override fun handleMessage(msg: Message) {
                        photoImageViewP.imageBitmap = bitmap
                        photoImageViewProfileUser.imageBitmap = bitmap
                    }
                }
                handler.sendEmptyMessage(0)
            }.addOnFailureListener {
                // TODO отображать ошибку
            }
    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data!!.data
            photoImageView.setImageURI(imageUri)
        }
    }

    private fun validate(email: String, password: String, name: String) =
        email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()
    }

    fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, text, duration).show()
    }

