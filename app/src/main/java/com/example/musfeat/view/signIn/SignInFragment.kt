package com.example.musfeat.view.signIn

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.musfeat.R
import com.example.musfeat.architecture.BaseFragment
import com.example.musfeat.presentation.SignInPresenter
import com.example.musfeat.view.MainActivity
import com.example.musfeat.view.signUp.SignUpFragment
import com.example.musfeat.view.swipe.SwipeFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*
import kotlinx.android.synthetic.main.fragment_sign_in.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : BaseFragment(R.layout.fragment_sign_in), SignInView {

    private var uEmail: String? = null
    private var uPassword: String? = null

    companion object {

        private const val ARG_EMAIL = "EMAIL"
        private const val ARG_PASSWORD = "PASSWORD"

        fun newInstance(email: String, password: String): SignInFragment {
            val fragment = SignInFragment()
            val args = Bundle()
            args.putSerializable(ARG_EMAIL, email)
            args.putSerializable(ARG_PASSWORD, password)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var loginPresenter: SignInPresenter
    private val presenter: SignInPresenter by moxyPresenter { loginPresenter }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        if (arguments != null) {
            uEmail = requireArguments().getSerializable(ARG_EMAIL) as String
            uPassword = requireArguments().getSerializable(ARG_PASSWORD) as String
            presenter.signIn(uEmail!!, uPassword!!)
        }
        if (auth.currentUser != null)
            toSwipeFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.toolbar?.title = getString(R.string.login_title)
        (activity as MainActivity).showBackBtn(false)
        (activity as MainActivity).showNavView(false)
        setListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setListeners() {

        etEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_email_24, 0, 0, 0)
        etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_lock_24, 0, 0, 0)

        etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val resources = context!!.resources
                val theme = context!!.theme
                if (p0?.length != 0 && presenter.isEmailValid(p0.toString())) {
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
                } else {
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
                if (p0?.toString()?.length!! >= 4) {
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
                } else {
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
                val mEmail: String = etEmail.text.toString().trim()
                val mPassword: String = etPassword.text.toString().trim()
                if (presenter.isEmailValid(mEmail) && mPassword.length >= 4) {
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

        onTouchListener(etEmail)
        onTouchListener(etPassword)

        btnLogin.setOnClickListener {
            presenter.signIn(etEmail.text.toString(), etPassword.text.toString())
        }

        btnRegistration.setOnClickListener {
            presenter.onBtnRegistrationClicked()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onTouchListener(editTextId: EditText) {
        editTextId.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN &&
                editTextId.compoundDrawables[2] != null &&
                event.x >= editTextId.right - editTextId.left - editTextId.compoundDrawables[2].bounds.width() &&
                editTextId.text.isNotEmpty()
            )
                editTextId.setText("")
            false
        }
    }

    override fun toSignUpFragment() {
        etEmail.setText("")
        etPassword.setText("")
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, SignUpFragment())
            .commit()
    }

    override fun toSwipeFragment() {
        etEmail.setText("")
        etPassword.setText("")
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, SwipeFragment())
            .commit()
    }

    override fun showError(message: String) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}
