package com.example.musfeat.view.signIn

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

@AddToEndSingle
interface SignInView : MvpView {

    @AddToEndSingle
    fun setListeners()

    @OneExecution
    fun toSignUpFragment()

    @OneExecution
    fun toSwipeFragment()

    @OneExecution
    fun showError(message: String)
}
