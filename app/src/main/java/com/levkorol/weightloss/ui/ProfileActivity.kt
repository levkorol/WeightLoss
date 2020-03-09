package com.levkorol.weightloss.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.levkorol.weightloss.R
import com.levkorol.weightloss.model.SongInfo
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initializeNavigation()
    }

    fun goToMenu(v: View) {
        val intent = Intent(this, LoginPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun initializeNavigation() {
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profileItem -> {
                    // TODO
                }
                else -> {}
            }
            return@setNavigationItemSelectedListener true
        }
    }






//    private fun NavigationView.setNavigationItemSelectedListener(profileActivity: ProfileActivity) {
//        when (it.itemId) {
//            R.id.nav_achivment -> {
//                val achivmentIntent = Intent(this, AchivmentActivity::class.java)
//                achivmentIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
//                startActivity(achivmentIntent)
//            }
//            R.id.nav_friends -> {
//                val friendsIntent = Intent(this, FriendsActivity::class.java)
//                friendsIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
//                startActivity(friendsIntent)
//            }
//            R.id.nav_alarm -> {
//                val alarmIntent = Intent(this, AlarmActivity::class.java)
//                alarmIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
//                startActivity(alarmIntent)
//            }
//            R.id.nav_myGoal -> {
//                val myGoalIntent = Intent(this, MyGoalActivity::class.java)
//                myGoalIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
//                startActivity(myGoalIntent)
//            }
//            R.id.nav_notes -> {
//                val notesIntent = Intent(this, NotesActivity::class.java)
//                notesIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
//                startActivity(notesIntent)
//            }
//            R.id.nav_schedule -> {
//                val scheduleIntent = Intent(this, ScheduleActivity::class.java)
//                scheduleIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
//                startActivity(scheduleIntent)
//            }
//
//        }
//
//        drawer_layout.closeDrawer(GravityCompat.START)
//        return true
//    }
}



