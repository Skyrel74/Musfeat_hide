package com.example.musfeat.view

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
import dagger.hilt.android.AndroidEntryPoint
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
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        email.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_email_24, 0, 0, 0)
        password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_lock_24, 0, 0, 0)
        email.addTextChangedListener(object : TextWatcher {
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
                    email.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    email.setCompoundDrawablesWithIntrinsicBounds(
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_email_24, theme),
                        null,
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_cancel_24, theme),
                        null
                    )
                } else if (p0.isEmpty()) {
                    email.setCompoundDrawablesRelativeWithIntrinsicBounds(
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
                    email.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    email.setCompoundDrawablesWithIntrinsicBounds(
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_email_24, theme),
                        null, null, null
                    )
                }
            }
        })

        password.addTextChangedListener(object : TextWatcher {
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
                    password.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    password.setCompoundDrawablesWithIntrinsicBounds(
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_lock_24, theme),
                        null,
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_cancel_24, theme),
                        null
                    )
                } else if (p0.isEmpty()) {
                    password.setCompoundDrawablesRelativeWithIntrinsicBounds(
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
                    password.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    password.setCompoundDrawablesWithIntrinsicBounds(
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_lock_24, theme),
                        null, null, null
                    )
                }
            }
        })

        email.setOnTouchListener { _, event ->

            if (event.action == MotionEvent.ACTION_DOWN && email.compoundDrawables[2] != null &&
                event.x >= email.right - email.left - email.compoundDrawables[2].bounds.width() &&
                email.text.isNotEmpty()
            )
                email.setText("")
            false
        }

        password.setOnTouchListener { _, event ->

            if (event.action == MotionEvent.ACTION_DOWN &&
                password.compoundDrawables[2] != null &&
                event.x >= password.right - password.left - password.compoundDrawables[2].bounds.width() &&
                password.text.isNotEmpty()
            )
                password.setText("")
            false
        }

        login_button.setBackgroundResource(R.color.colorWhiteBlueShade)

        val loginTextWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                val mUsername: String = email.text.toString().trim()
                val mPassword: String = password.text.toString().trim()
                if (mUsername.isNotEmpty() && mPassword.isNotEmpty())
                    login_button.setBackgroundResource(R.color.colorDarkBlue)
                else
                    login_button.setBackgroundResource(R.color.colorWhiteBlueShade)
            }
        }

        email.addTextChangedListener(loginTextWatcher)
        password.addTextChangedListener(loginTextWatcher)
    }
}