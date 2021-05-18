package com.example.musfeat.view.message

import android.os.Bundle
import com.example.musfeat.architecture.BaseView
import com.xwray.groupie.kotlinandroidextensions.Item
import moxy.viewstate.strategy.alias.AddToEndSingle

interface MessageView : BaseView {

    @AddToEndSingle
    fun setSettingsFragment(savedInstanceState: Bundle?)

    @AddToEndSingle
    fun updateRecyclerView(messages: List<Item>)
}