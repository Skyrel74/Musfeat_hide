package com.example.musfeat.view.chat

import com.example.musfeat.architecture.BaseView
import com.xwray.groupie.kotlinandroidextensions.Item
import moxy.viewstate.strategy.alias.AddToEndSingle

interface ChatView : BaseView {

    @AddToEndSingle
    fun updateRecyclerView(items: List<Item>)
}