package com.example.musfeat.presentation

import com.example.musfeat.architecture.BasePresenter
import com.example.musfeat.view.login.LoginView
import javax.inject.Inject

class LoginPresenter @Inject constructor() : BasePresenter<LoginView>() {

    fun onBtnRegistrationClicked(){
        viewState.toRegistrationFragment()
    }

}