package com.example.musfeat.view.swipe

import android.os.Bundle
import android.view.View
import com.example.musfeat.R
import com.example.musfeat.architecture.BaseFragment
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*

@AndroidEntryPoint
class SwipeFragment : BaseFragment(R.layout.fragment_swipe) {

    private lateinit var userListenerRegistration: ListenerRegistration

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.toolbar?.title = getString(R.string.cards_title)
    }
}
