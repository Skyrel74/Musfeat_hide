package com.example.musfeat.view

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import moxy.viewstate.strategy.alias.Skip

interface MainView : MvpView {

    @AddToEndSingle
    fun setListeners()

    @AddToEndSingle
    fun showNavView(isVisible: Boolean)

    @AddToEndSingle
    fun showBackBtn(isVisible: Boolean = true)

    @Skip
    fun setStartupFragment()

    @OneExecution
    fun showProgressBar(isVisible: Boolean = true)
}
