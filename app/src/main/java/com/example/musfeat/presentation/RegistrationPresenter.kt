package com.example.musfeat.presentation

import com.example.musfeat.architecture.BasePresenter
import com.example.musfeat.view.registration.RegistrationView
import javax.inject.Inject

class RegistrationPresenter @Inject constructor() : BasePresenter<RegistrationView>() {

    fun checkPasswordEquality(password: String, passwordRepeat: String) {

        if (password.isEmpty() && passwordRepeat.isEmpty()) {
            viewState.onPasswordEmpty()
        } else if (password == passwordRepeat) {
            viewState.onPasswordEqual()
        } else {
            viewState.onPasswordNotEqual()
        }


    }
}