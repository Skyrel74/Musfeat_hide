package com.example.musfeat.presentation

import android.util.Log
import com.example.musfeat.architecture.BasePresenter
import com.example.musfeat.service.MyFirebaseMessagingService
import com.example.musfeat.util.FirestoreUtil
import com.example.musfeat.view.signIn.SignInView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import java.util.regex.Pattern
import javax.inject.Inject

class SignInPresenter @Inject constructor() : BasePresenter<SignInView>() {

    fun signIn(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    FirebaseMessaging.getInstance().token.addOnCompleteListener {
                        FirestoreUtil.updateCurrentUser(registrationTokens = it.result)
                        MyFirebaseMessagingService.addTokenToFirestore(it.result)
                        Log.d("FCM", it.result.toString())
                    }
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