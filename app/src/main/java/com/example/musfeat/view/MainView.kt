package com.example.musfeat.view

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface MainView : MvpView {

    /**
     * Set fragment listeners
     */
    @AddToEndSingle
    fun setListeners()

    /**
     * Show navigation view by [isVisible] condition
     */
    @AddToEndSingle
    fun showNavView(isVisible: Boolean)

    /**
     * Show back navigation button by [isVisible] condition
     */
    @AddToEndSingle
    fun showBackBtn(isVisible: Boolean = true)

    /**
     * Set fragment settings
     */
    @OneExecution
    fun setStartupFragment()

    /**
     * Show progress bar by [isVisible] condition
     */
    @OneExecution
    fun showProgressBar(isVisible: Boolean = true)
}
