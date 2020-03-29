package com.levkorol.weightloss.data

import android.app.usage.EventStats
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.CalendarContract
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.levkorol.weightloss.model.EventsB
import com.levkorol.weightloss.ui.ProfileUserActivity
import com.levkorol.weightloss.ui.showToast
import kotlinx.android.synthetic.main.activity_profile_user.*
import kotlinx.android.synthetic.main.activity_sign_up_with_email.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.imageBitmap
import org.greenrobot.eventbus.ThreadMode

object UserRepository {

    val TAG = UserRepository::class.java.simpleName

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { Firebase.firestore }
    private val storage: FirebaseStorage by lazy { Firebase.storage }

    var premium: Boolean = false

    fun setPremium(context: Context, premium: Boolean) {
        this.premium = premium
        EventBus.getDefault().post(EventsB())
        // TODO и на сервере обновляем ещё
        db.collection("users")
            .whereEqualTo("email", auth.currentUser?.email)
            .get()
            .addOnSuccessListener { result ->
                if (result.documents.size == 0) {

                } else {
                    Log.v(UserRepository.TAG, "loadInfo#succes: ${result.documents[0].get("name")}")
                    premiumUpdate()
                    updatePremium(result.documents[0].id)
                }
            }
            .addOnFailureListener { exception ->
                // TODO отображать ошибку
                Log.w(ProfileUserActivity.TAG, "Error getting documents.", exception)
            }
    }

    private fun premiumUpdate() {
        val premiumUser = premium
        "premium" to premiumUser
    }

    private fun updatePremium(documentId: String) {
        val washingtonRef = db.collection("users").document("premium")
        washingtonRef
            .update("premium", true)
            .addOnSuccessListener {
                Log.d(
                    TAG.toString(),
                    "DocumentSnapshot successfully updated!"
                )
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
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

}