package com.example.musfeat.view

import android.os.Bundle
import androidx.core.view.isVisible
import com.example.musfeat.AppConstants
import com.example.musfeat.R
import com.example.musfeat.architecture.BaseActivity
import com.example.musfeat.view.calendar.CalendarFragment
import com.example.musfeat.view.chat.ChatFragment
import com.example.musfeat.view.map.MapFragment
import com.example.musfeat.view.message.MessageFragment
import com.example.musfeat.view.profile.ProfileFragment
import com.example.musfeat.view.signIn.SignInFragment
import com.example.musfeat.view.signUp.SignUpFragment
import com.example.musfeat.view.swipe.SwipeFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*

@AndroidEntryPoint
class MainActivity : BaseActivity(), MainView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wrapper)
        setSupportActionBar(toolbar)
        setListeners()
        if (savedInstanceState == null)
            setStartupFragment()
    }

    override fun setStartupFragment() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            if (intent.hasExtra(AppConstants.NOTIFICATION_FLAG_CHAT)) {
                val uName = intent.getStringExtra(AppConstants.USER_NAME)
                val uId = intent.getStringExtra(AppConstants.USER_ID)
                val channelId = intent.getStringExtra(AppConstants.CHANNEL_ID)
                if (uName != null && uId != null && channelId != null)
                    supportFragmentManager.beginTransaction()
                        .add(R.id.container, MessageFragment.newInstance(uName, uId, channelId))
                        .commit()
            }
            supportFragmentManager.beginTransaction()
                .add(R.id.container, SwipeFragment())
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, SignInFragment())
                .commit()
        }
    }

    override fun setListeners() {
        backIv.setOnClickListener {
            if (it.isVisible) {
                when (supportFragmentManager.findFragmentById(R.id.container)) {
                    is SignUpFragment -> supportFragmentManager.beginTransaction()
                        .replace(R.id.container, SignInFragment())
                        .commit()
                    else -> {
                        navView.selectedItemId = R.id.navigation_chat
                    }
                }
            }
        }

        if (navView.isVisible)
            navView?.selectedItemId = R.id.navigation_cards
            navView.setOnNavigationItemSelectedListener OnNavigationItemSelectedListener@{ item ->
                when (item.itemId) {
                    R.id.navigation_cards -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, SwipeFragment())
                            .commit()
                        return@OnNavigationItemSelectedListener true

                    }
                    R.id.navigation_map -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, MapFragment())
                            .commit()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_chat -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, ChatFragment())
                            .commit()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_calendar -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, CalendarFragment())
                            .commit()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_profile -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, ProfileFragment())
                            .commit()
                        return@OnNavigationItemSelectedListener true
                    }
                }
                return@OnNavigationItemSelectedListener false
            }
    }

    override fun showProgressBar(isVisible: Boolean) {
        pbLoading.isVisible = isVisible
    }

    override fun showBackBtn(isVisible: Boolean) {
        backIv.isVisible = isVisible
    }

    override fun showNavView(isVisible: Boolean) {
        navView.isVisible = isVisible
    }
}
