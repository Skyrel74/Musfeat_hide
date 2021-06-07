package com.example.musfeat.view.signUp

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

@AddToEndSingle
interface SignUpView : MvpView {

    @OneExecution
    fun toSignInFragment()

    @OneExecution
    fun showError(message: String)
}
