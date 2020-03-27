package com.levkorol.weightloss.data

import android.app.usage.EventStats
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.levkorol.weightloss.ui.ProfileUserActivity
import kotlinx.android.synthetic.main.activity_profile_user.*
import kotlinx.android.synthetic.main.activity_sign_up_with_email.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.imageBitmap

object UserRepository {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { Firebase.firestore }
    private val storage: FirebaseStorage by lazy { Firebase.storage }

    var premium: Boolean = false

    fun setPremium(context: Context, premium: Boolean) {
        this.premium = premium
        //  EventBus.getDefault().post(EventSample())
        // TODO отправляем событие
        // TODO и на сервере обновляем ещё
//       db.collection("users")
//           .
    }


    fun updatePhotoProfile(i: ImageView) {
        val reference = storage.reference.child("images/${auth.currentUser?.email}")
        Log.v(ProfileUserActivity.TAG, "Image ${reference}")
        val ONE_MEGABYTE = 1024 * 1024
        reference.getBytes(ONE_MEGABYTE.toLong())
            .addOnSuccessListener {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                val handler = object : Handler(Looper.getMainLooper()) {
                    override fun handleMessage(msg: Message) {
                        i.imageBitmap = bitmap
                    }
                }
                handler.sendEmptyMessage(0)
            }.addOnFailureListener {
                // TODO отображать ошибку
            }
    }

    private fun premiumUpdate(){
        val premiumUser = if ( premium ) { 1 } else { 0 }

        "premium" to premiumUser
    }

}