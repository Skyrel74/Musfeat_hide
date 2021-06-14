package com.example.musfeat.view.swipe

import com.example.musfeat.data.User
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface SwipeView : MvpView {

    /**
     * Set data to cards by users [dataSet]
     */
    @AddToEndSingle
    fun setData(dataSet: List<User>)
}
