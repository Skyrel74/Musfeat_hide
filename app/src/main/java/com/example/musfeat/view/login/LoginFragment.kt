package com.example.musfeat.view.login

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.musfeat.R
import com.example.musfeat.architecture.BaseFragment
import com.example.musfeat.presentation.LoginPresenter
import com.example.musfeat.view.registration.RegistrationFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*
import kotlinx.android.synthetic.main.fragment_login.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login), LoginView {

    @Inject
    lateinit var loginPresenter: LoginPresenter

    private val presenter: LoginPresenter by moxyPresenter { loginPresenter }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        activity?.toolbar?.title = getString(R.string.login_title)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {


        btnRegistration.setOnClickListener {
            presenter.onBtnRegistrationClicked()
        }

        etEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_email_24, 0, 0, 0)
        etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_lock_24, 0, 0, 0)
        etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val resources = context!!.resources
                val theme = context!!.theme
                if (p0?.length != 0) {
                    var drawable =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_email_24, theme)
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(
                        drawable,
                        ResourcesCompat.getColor(resources, R.color.colorDarkBlue, theme)
                    )
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    etEmail.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    etEmail.setCompoundDrawablesWithIntrinsicBounds(
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_email_24, theme),
                        null,
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_cancel_24, theme),
                        null
                    )
                } else if (p0.isEmpty()) {
                    etEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_email_24, 0, 0, 0
                    )
                    var drawable =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_email_24, theme)
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(
                        drawable!!,
                        ResourcesCompat.getColor(resources, R.color.colorDefault, theme)
                    )
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    etEmail.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    etEmail.setCompoundDrawablesWithIntrinsicBounds(
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_email_24, theme),
                        null, null, null
                    )
                }
            }
        })

        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val resources = context!!.resources
                val theme = context!!.theme
                if (p0?.length != 0) {
                    var drawable =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_lock_24, theme)
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(
                        drawable!!,
                        ResourcesCompat.getColor(resources, R.color.colorDarkBlue, theme)
                    )
                    DrawableCompat.setTintMode(drawable!!, PorterDuff.Mode.SRC_IN)
                    etPassword.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    etPassword.setCompoundDrawablesWithIntrinsicBounds(
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_lock_24, theme),
                        null,
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_cancel_24, theme),
                        null
                    )
                } else if (p0.isEmpty()) {
                    etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_lock_24,
                        0, 0, 0
                    )
                    var drawable =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_lock_24, theme)
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(
                        drawable!!,
                        ResourcesCompat.getColor(resources, R.color.colorDefault, theme)
                    )
                    DrawableCompat.setTintMode(drawable!!, PorterDuff.Mode.SRC_IN)
                    etPassword.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    etPassword.setCompoundDrawablesWithIntrinsicBounds(
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_lock_24, theme),
                        null, null, null
                    )
                }
            }
        })

        etEmail.setOnTouchListener { _, event ->

            if (event.action == MotionEvent.ACTION_DOWN && etEmail.compoundDrawables[2] != null &&
                event.x >= etEmail.right - etEmail.left - etEmail.compoundDrawables[2].bounds.width() &&
                etEmail.text.isNotEmpty()
            )
                etEmail.setText("")
            false
        }

        etPassword.setOnTouchListener { _, event ->

            if (event.action == MotionEvent.ACTION_DOWN &&
                etPassword.compoundDrawables[2] != null &&
                event.x >= etPassword.right - etPassword.left - etPassword.compoundDrawables[2].bounds.width() &&
                etPassword.text.isNotEmpty()
            )
                etPassword.setText("")
            false
        }

        btnLogin.setBackgroundResource(R.color.colorWhiteBlueShade)

        val loginTextWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                val mUsername: String = etEmail.text.toString().trim()
                val mPassword: String = etPassword.text.toString().trim()
                if (mUsername.isNotEmpty() && mPassword.isNotEmpty()) {
                    btnLogin.setBackgroundResource(R.color.colorDarkBlue)
                    btnLogin.isEnabled = true
                } else {
                    btnLogin.setBackgroundResource(R.color.colorWhiteBlueShade)
                    btnLogin.isEnabled = false
                }
            }
        }

        etEmail.addTextChangedListener(loginTextWatcher)
        etPassword.addTextChangedListener(loginTextWatcher)


    }

    override fun toRegistrationFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, RegistrationFragment.newInstance())
            .addToBackStack("LoginFragment")
            .commit()
    }
}