package com.example.musfeat.view

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface MainView : MvpView {

    @AddToEndSingle
    fun setListeners()

    @AddToEndSingle
    fun showNavView(isVisible: Boolean)

    @AddToEndSingle
    fun showBackBtn(isVisible: Boolean = true)

    @AddToEndSingle
    fun setStartupFragment()

    @OneExecution
    fun showProgressBar(isVisible: Boolean = true)
}
