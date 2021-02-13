package com.example.musfeat.view.login

import com.example.musfeat.architecture.BaseView
import moxy.viewstate.strategy.alias.OneExecution

interface LoginView : BaseView {

    @OneExecution
    fun toRegistrationFragment()

}
