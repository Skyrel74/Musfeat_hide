package com.example.musfeat.view.signUp

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

@AddToEndSingle
interface SignUpView : MvpView {

    /**
     * Go to sign in fragment
     */
    @OneExecution
    fun toSignInFragment()

    /**
     * Show error [message]
     */
    @OneExecution
    fun showError(message: String)
}
