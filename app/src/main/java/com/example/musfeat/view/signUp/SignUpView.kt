package com.example.musfeat.view.signUp

import com.example.musfeat.architecture.BaseView
import moxy.viewstate.strategy.alias.OneExecution
import moxy.viewstate.strategy.alias.SingleState

interface SignUpView : BaseView {

    @SingleState
    fun onPasswordNotEqual()

    @SingleState
    fun onPasswordEqual()

    @SingleState
    fun onPasswordEmpty()

    @SingleState
    fun onNameEmpty()

    @SingleState
    fun onNameHas()


    @SingleState
    fun onSurnameEmpty()

    @SingleState
    fun onSurnameHas()


    @SingleState
    fun onEmailEmpty()

    @SingleState
    fun onEmailHas()

    @OneExecution
    fun showError(message: String)

    @OneExecution
    fun toMenuActivity()
}