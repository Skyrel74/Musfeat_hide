package com.example.musfeat.view.chat

import com.example.musfeat.data.User
import com.example.musfeat.view.recyclerview.item.ChatItem
import com.xwray.groupie.kotlinandroidextensions.Item
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

@AddToEndSingle
interface ChatView : MvpView {

    @AddToEndSingle
    fun updateRecyclerView(items: List<Item>)

    @OneExecution
    fun toMessageFragment(secondUser: User, item: ChatItem)
}