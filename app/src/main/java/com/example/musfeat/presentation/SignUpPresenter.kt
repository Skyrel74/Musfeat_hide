package com.example.musfeat.presentation

import com.example.musfeat.architecture.BasePresenter
import com.example.musfeat.view.signUp.SignUpView
import javax.inject.Inject

class SignUpPresenter @Inject constructor() : BasePresenter<SignUpView>() {

    fun checkPasswordEquality(password: String, passwordRepeat: String) {

        if (password.isEmpty() && passwordRepeat.isEmpty()) viewState.onPasswordEmpty()
        else if (password == passwordRepeat) viewState.onPasswordEqual()
        else viewState.onPasswordNotEqual()

    }

    fun checkName(data: String) {
        if (data.isEmpty()) viewState.onNameEmpty()
        else viewState.onNameHas()
    }

    fun checkSurname(data: String) {
        if (data.isEmpty()) viewState.onSurnameEmpty()
        else viewState.onSurnameHas()
    }

    fun checkEmail(data: String) {
        if (data.isEmpty()) viewState.onEmailEmpty()
        else viewState.onEmailHas()
    }

    fun checkData(
        name: String, surname: String, email: String, password: String, repeatPassword: String,
        guitarSkill: Boolean, vocalSkill: Boolean, drumsSkill: Boolean
    ) {

        if (name.isEmpty()) {
            viewState.showError("Заполните поле 'Имя'")
        } else if (surname.isEmpty()) {
            viewState.showError("Заполните поле 'Фамилия'")
        } else if (email.isEmpty()) {
            viewState.showError("Заполните поле 'Email'")
        } else if (password.isEmpty() || repeatPassword.isEmpty()) {
            viewState.showError("Заполните поле 'Пароль'")
        } else if (password != repeatPassword) {
            viewState.showError("Пароли не совпадают")
        } else {
            viewState.toMenuActivity()
        }
    }

}