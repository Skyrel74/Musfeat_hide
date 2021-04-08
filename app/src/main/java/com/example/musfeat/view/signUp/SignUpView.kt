package com.example.musfeat.view.signUp

import com.example.musfeat.architecture.BaseView
import moxy.viewstate.strategy.alias.OneExecution

interface SignUpView : BaseView {

    @OneExecution
    fun showError(message: String)

    @OneExecution
    fun toSignInFragment(uid: String, email: String)
}