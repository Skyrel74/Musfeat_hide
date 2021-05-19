package com.example.musfeat.view.signIn

import com.example.musfeat.architecture.BaseView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface SignInView : BaseView {

    @AddToEndSingle
    fun setListeners()

    @OneExecution
    fun toSignUpFragment()

    @OneExecution
    fun toSwipeFragment()

    @OneExecution
    fun showError(message: String)
}
