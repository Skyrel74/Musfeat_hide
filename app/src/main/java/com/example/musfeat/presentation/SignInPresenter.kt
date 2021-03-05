package com.example.musfeat.presentation

import com.example.musfeat.architecture.BasePresenter
import com.example.musfeat.view.signIn.SignInView
import java.util.regex.Pattern
import javax.inject.Inject

class SignInPresenter @Inject constructor() : BasePresenter<SignInView>() {

    fun isEmailValid(email: String): Boolean =
        email.isNotEmpty() && Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{1,25}" +
                    ")+"
        ).matcher(email).matches()

    fun onBtnRegistrationClicked() = viewState.toSignUpFragment()
}