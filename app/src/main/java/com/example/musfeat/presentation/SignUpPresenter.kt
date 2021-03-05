package com.example.musfeat.presentation

import com.example.musfeat.architecture.BasePresenter
import com.example.musfeat.view.signUp.SignUpView
import java.util.regex.Pattern
import javax.inject.Inject

class SignUpPresenter @Inject constructor() : BasePresenter<SignUpView>() {

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

    fun isPasswordValid(password: String): Boolean = when {
        password.isEmpty() -> false
        password.length < 5 -> false
        else -> true
    }

    fun checkData(
        name: String, surname: String, email: String, password: String, repeatPassword: String
    ) = when {
        name.isEmpty() -> viewState.showError("Заполните поле 'Имя'")
        surname.isEmpty() -> viewState.showError("Заполните поле 'Фамилия'")
        email.isEmpty() -> viewState.showError("Заполните поле 'Email'")
        !isEmailValid(email) -> viewState.showError("Заполните поле 'Email' по шаблону example@mail.com")
        password.isEmpty() || repeatPassword.isEmpty() -> viewState.showError("Заполните поля 'Пароль'")
        password != repeatPassword -> viewState.showError("Пароли не совпадают")
        else -> viewState.toSignInFragment()
    }
}