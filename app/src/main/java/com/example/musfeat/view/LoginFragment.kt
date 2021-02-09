package com.example.musfeat.view

import com.example.musfeat.R
import com.example.musfeat.architecture.BaseFragment
import com.example.musfeat.presentation.LoginPresenter
import dagger.hilt.android.AndroidEntryPoint
import moxy.ktx.moxyPresenter
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login), LoginView {

    @Inject
    lateinit var loginPresenter: LoginPresenter

    private val presenter: LoginPresenter by moxyPresenter { loginPresenter }
}