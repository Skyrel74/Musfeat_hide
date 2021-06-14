package com.example.musfeat.view.chat

import com.example.musfeat.data.User
import com.example.musfeat.view.recyclerview.item.ChatItem
import com.xwray.groupie.kotlinandroidextensions.Item
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

@AddToEndSingle
interface ChatView : MvpView {

    /**
     * Update recyclerview by [items] list
     */
    @AddToEndSingle
    fun updateRecyclerView(items: List<Item>)

    /**
     * Go to message fragment
     * [secondUser] is user with whom there is a chat
     * [item] is the chat itself
     */
    @OneExecution
    fun toMessageFragment(secondUser: User, item: ChatItem)
}