package com.example.musfeat.presentation

import com.example.musfeat.architecture.BasePresenter
import com.example.musfeat.data.MusicalInstrument
import com.example.musfeat.data.User
import com.example.musfeat.view.signUp.SignUpView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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
    ): Any = when {
        name.isEmpty() -> viewState.showError("Заполните поле 'Имя'")
        surname.isEmpty() -> viewState.showError("Заполните поле 'Фамилия'")
        email.isEmpty() -> viewState.showError("Заполните поле 'Email'")
        !isEmailValid(email) -> viewState.showError("Заполните поле 'Email' по шаблону example@mail.com")
        password.isEmpty() || repeatPassword.isEmpty() -> viewState.showError("Заполните поля 'Пароль'")
        password != repeatPassword -> viewState.showError("Пароли не совпадают")
        else -> {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        val firebaseUser = FirebaseAuth.getInstance().currentUser
                        val user = User(
                            firebaseUser.uid,
                            name,
                            surname,
                            email,
                            listOf(MusicalInstrument.GUITAR)
                        )
                        FirebaseDatabase.getInstance().getReference("users")
                            .child(FirebaseAuth.getInstance().currentUser.uid)
                            .setValue(user)

                        viewState.toSignInFragment(email, password)
                    } else {
                        viewState.showError("Создать аккаунт не получилось, попробуйте позже")
                    }
                }
        }
    }
}
