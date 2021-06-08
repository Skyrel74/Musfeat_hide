package com.example.musfeat.view.swipe

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.view.isGone
import com.example.musfeat.R
import com.example.musfeat.architecture.BaseFragment
import com.example.musfeat.architecture.BaseView
import com.example.musfeat.data.MusicalInstrument
import com.example.musfeat.data.User
import com.example.musfeat.util.FirestoreUtil
import com.example.musfeat.view.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.yuyakaido.android.cardstackview.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*
import kotlinx.android.synthetic.main.fragment_swipe.*
import org.jetbrains.anko.connectivityManager
import org.jetbrains.anko.support.v4.runOnUiThread

@AndroidEntryPoint
class SwipeFragment : BaseFragment(R.layout.fragment_swipe), BaseView {

    private lateinit var userListenerRegistration: ListenerRegistration
    private var cardStackAdapter: CardStackAdapter? = null
    private var cardStackLayoutManager: CardStackLayoutManager? = null
    private var isCardStackSet = false


    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            if (isCardStackSet) {
                runOnUiThread { showNoInternetError(false) }
            } else {
                runOnUiThread {
                    setupCardStackView().also { Log.d("qweqweqwe", "onAvailable: ") }
                    setUsers()
                    setupCardStackViewButtons()
                    showNoInternetError(false)
                    isCardStackSet = true
                }
            }
        }

        override fun onLost(network: Network) {
            runOnUiThread { showNoInternetError(true) }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        context?.connectivityManager?.registerDefaultNetworkCallback(networkCallback)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.toolbar?.title = getString(R.string.cards_title)
        (activity as MainActivity).showNavView(true)
        (activity as MainActivity).showBackBtn(false)


        if (isInternetAvailable()) {
            setupCardStackView().also { Log.d("qweqweqwe", "onViewCreated:  ") }
            setUsers()
            setupCardStackViewButtons()
            isCardStackSet = true
        } else {
            showNoInternetError(true)
        }
    }

    private fun showNoInternetError(isInternetAvailable: Boolean) {
        groupCardStackView.isGone = isInternetAvailable
        groupNoInternet.isGone = !isInternetAvailable
    }


    private fun setupCardStackViewButtons() {
        ibAccept.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            cardStackLayoutManager?.setSwipeAnimationSetting(setting)
            csvSwipe?.swipe()
        }

        ibCancel.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            cardStackLayoutManager?.setSwipeAnimationSetting(setting)
            csvSwipe?.swipe()
        }
    }

    private fun setUsers() {
        FirestoreUtil.getRandomUsers { dataSet ->
            FirestoreUtil.getLikedUsers(FirebaseAuth.getInstance().currentUser!!.uid) { likedUsers ->
                FirestoreUtil.getDislikedUsers(FirebaseAuth.getInstance().currentUser!!.uid) { dislikedUsers ->
                    val iterator = dataSet.iterator()
                    while (iterator.hasNext()) {
                        val user = iterator.next()
                        if (likedUsers.contains(user.uid)
                            || dislikedUsers.contains(user.uid)
                            || user.uid == FirebaseAuth.getInstance().currentUser!!.uid
                        )
                            iterator.remove()
                    }
                    FirestoreUtil.getSearchSettings { settings ->
                        if (!compare(listOf(MusicalInstrument.NONE), settings)) {
                            dataSet.find {
                                !compare(it.musicalInstrument, settings)
                            }?.let { dataSet.remove(it) }
                        }
                        cardStackAdapter?.submitList(dataSet as List<User>)
                    }
                }
            }
        }
    }

    private fun setupCardStackView() {
        with(csvSwipe) {
            layoutManager = CardStackLayoutManager(requireContext(), object : CardStackListener {

                lateinit var swipedUser: User

                override fun onCardSwiped(direction: Direction?) = when (direction) {
                    Direction.Right ->
                        FirestoreUtil.setLikedUser(swipedUser)
                    else ->
                        FirestoreUtil.setDislikedUser(swipedUser)
                }

                override fun onCardDisappeared(view: View?, position: Int) {
                    swipedUser = cardStackAdapter?.currentList?.get(position)!!
                }

                override fun onCardAppeared(view: View?, position: Int) {}
                override fun onCardDragging(direction: Direction?, ratio: Float) {}
                override fun onCardCanceled() {}
                override fun onCardRewound() {}

            }).also {
                cardStackLayoutManager = it
            }
            adapter = CardStackAdapter().also {
                cardStackAdapter = it
            }
        }
    }

    private fun <T> compare(list1: List<T>, list2: List<T>): Boolean {
        list1.forEach { elem1 ->
            if (list2.toString().contains(elem1.toString()))
                return@compare true
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cardStackAdapter = null
        cardStackLayoutManager = null
    }

    override fun onDestroy() {
        context?.connectivityManager?.unregisterNetworkCallback(networkCallback)
        super.onDestroy()
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = requireContext().connectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }
}
