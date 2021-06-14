package com.example.musfeat.view.signIn

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

@AddToEndSingle
interface SignInView : MvpView {

    /**
     * Set sign in listeners
     */
    @AddToEndSingle
    fun setListeners()

    /**
     * Go to sign up fragment
     */
    @OneExecution
    fun toSignUpFragment()

    /**
     * Go to swipe fragment
     */
    @OneExecution
    fun toSwipeFragment()

    /**
     * Show error [message]
     */
    @OneExecution
    fun showError(message: String)
}
