package com.example.musfeat.view.swipe

import android.os.Bundle
import android.view.View
import com.example.musfeat.R
import com.example.musfeat.architecture.BaseFragment
import com.example.musfeat.architecture.BaseView
import com.example.musfeat.util.FirestoreUtil
import com.example.musfeat.view.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*
import kotlinx.android.synthetic.main.fragment_swipe.*

@AndroidEntryPoint
class SwipeFragment : BaseFragment(R.layout.fragment_swipe), BaseView {

    private lateinit var userListenerRegistration: ListenerRegistration
    private var cardStackAdapter: CardStackAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.toolbar?.title = getString(R.string.cards_title)
        (activity as MainActivity).showNavView(true)
        (activity as MainActivity).showBackBtn(false)

        with(csvSwipe) {
            layoutManager = CardStackLayoutManager(requireContext())
            adapter = CardStackAdapter().also {
                cardStackAdapter = it
            }
        }

        FirestoreUtil.getRandomUsers { dataSet ->
            FirestoreUtil.getLikedUsers(FirebaseAuth.getInstance().currentUser!!.uid) { likedUsers ->
                FirestoreUtil.getDislikedUsers(FirebaseAuth.getInstance().currentUser!!.uid) { dislikedUsers ->
                    dataSet.forEach { user ->
                        if (likedUsers.contains(user.uid) || dislikedUsers.contains(user.uid) ||
                            user.uid == FirebaseAuth.getInstance().currentUser!!.uid
                        )
                            dataSet.remove(user)
                    }
                    cardStackAdapter?.setData(dataSet)
                }
            }
        }


        ibAccept.setOnClickListener {

        }

        ibCancel.setOnClickListener {

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        cardStackAdapter = null
    }
}
