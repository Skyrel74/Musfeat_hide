package com.example.musfeat.presentation

import com.example.musfeat.data.MusicalInstrument
import com.example.musfeat.data.User
import com.example.musfeat.util.FirestoreUtil
import com.example.musfeat.view.swipe.SwipeView
import com.google.firebase.auth.FirebaseAuth
import moxy.MvpPresenter
import javax.inject.Inject

class SwipePresenter @Inject constructor() : MvpPresenter<SwipeView>() {

    fun setData() {
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
                        viewState.setData(dataSet as List<User>)
                    }
                }
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
}
