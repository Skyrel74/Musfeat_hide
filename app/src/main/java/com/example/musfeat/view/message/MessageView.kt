package com.example.musfeat.view.message

import com.xwray.groupie.kotlinandroidextensions.Item
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface MessageView : MvpView {

    @AddToEndSingle
    fun setSettingsFragment()

    @AddToEndSingle
    fun updateRecyclerView(items: List<Item>)
}
