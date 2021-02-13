package com.example.musfeat.presentation

import com.example.musfeat.architecture.BasePresenter
import com.example.musfeat.view.signIn.SignInView
import javax.inject.Inject

class SignInPresenter @Inject constructor() : BasePresenter<SignInView>() {

    fun onBtnRegistrationClicked(){
        viewState.toRegistrationFragment()
    }

}