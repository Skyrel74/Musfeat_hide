package com.example.musfeat.view.swipe

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.example.musfeat.R
import com.example.musfeat.data.User
import com.example.musfeat.presentation.SwipePresenter
import com.example.musfeat.util.FirestoreUtil
import com.example.musfeat.view.MainActivity
import com.yuyakaido.android.cardstackview.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*
import kotlinx.android.synthetic.main.fragment_swipe.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject

@AndroidEntryPoint
class SwipeFragment : MvpAppCompatFragment(R.layout.fragment_swipe), SwipeView {

    private var cardStackAdapter: CardStackAdapter? = null
    private var cardStackLayoutManager: CardStackLayoutManager? = null

    @Inject
    lateinit var swipePresenter: SwipePresenter
    private val presenter: SwipePresenter by moxyPresenter { swipePresenter }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.toolbar?.title = getString(R.string.cards_title)
        (activity as MainActivity).showNavView(true)
        (activity as MainActivity).showBackBtn(false)

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

        presenter.setData()
    }

    override fun setData(dataSet: List<User>) {
        cardStackAdapter?.submitList(dataSet)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cardStackAdapter = null
        cardStackLayoutManager = null
    }
}
