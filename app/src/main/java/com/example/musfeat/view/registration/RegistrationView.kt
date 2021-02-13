package com.example.musfeat.view.registration

import android.widget.EditText
import com.example.musfeat.architecture.BaseView
import moxy.viewstate.strategy.alias.SingleState
import moxy.viewstate.strategy.alias.Skip

interface RegistrationView : BaseView {

    @SingleState
    fun onPasswordNotEqual()

    @SingleState
    fun onPasswordEqual()

    @SingleState
    fun onPasswordEmpty()

}