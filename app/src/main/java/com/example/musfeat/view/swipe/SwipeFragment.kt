package com.example.musfeat.view.swipe

import android.os.Bundle
import android.view.View
import com.example.musfeat.R
import com.example.musfeat.architecture.BaseFragment
import com.example.musfeat.view.signIn.SignInFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*
import kotlinx.android.synthetic.main.fragment_swipe.*

@AndroidEntryPoint
class SwipeFragment : BaseFragment(R.layout.fragment_swipe) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.toolbar?.title = "Поиск музыкантов"
    }

    override fun onStart() {
        super.onStart()

        logOutBtn.setOnClickListener(View.OnClickListener {
            FirebaseAuth.getInstance().signOut()
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, SignInFragment())
                .commit()
        })
    }
}