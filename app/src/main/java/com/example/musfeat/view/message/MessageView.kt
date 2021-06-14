package com.example.musfeat.view.message

import com.xwray.groupie.kotlinandroidextensions.Item
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface MessageView : MvpView {

    /**
     * Set settings for fragment
     */
    @AddToEndSingle
    fun setSettingsFragment()

    /**
     * Update recyclerview by [items] list
     */
    @AddToEndSingle
    fun updateRecyclerView(items: List<Item>)
}
