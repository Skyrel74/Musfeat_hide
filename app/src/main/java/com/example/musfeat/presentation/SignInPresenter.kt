package com.example.musfeat.presentation

import com.example.musfeat.architecture.BasePresenter
import com.example.musfeat.view.signIn.SignInView
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern
import javax.inject.Inject

class SignInPresenter @Inject constructor() : BasePresenter<SignInView>() {

    fun signIn(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewState.toSwipeFragment()
                } else {
                    viewState.showError("Неверный логин или пароль")
                }
            }
    }

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