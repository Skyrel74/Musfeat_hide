package com.example.musfeat.presentation

import com.example.musfeat.util.FirestoreUtil
import com.example.musfeat.view.chat.ChatView
import com.example.musfeat.view.recyclerview.item.ChatItem
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.OnItemClickListener
import moxy.MvpPresenter
import javax.inject.Inject

class ChatPresenter @Inject constructor() : MvpPresenter<ChatView>() {

    fun onItemClick() = OnItemClickListener { item, _ ->
        if (item is ChatItem) {
            val secondId: String =
                item.chat.usersIds.filter { it != FirebaseAuth.getInstance().currentUser!!.uid }[0]
            FirestoreUtil.getUserByUid(secondId) { secondUser ->
                viewState.toMessageFragment(secondUser, item)
            }
        }
    }
}