package com.example.musfeat.view.signIn

import com.example.musfeat.architecture.BaseView
import moxy.viewstate.strategy.alias.OneExecution

interface SignInView : BaseView {

    @OneExecution
    fun toRegistrationFragment()

}
