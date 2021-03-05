package com.example.musfeat.view.signUp

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
import com.example.musfeat.MainActivity
import com.example.musfeat.R
import com.example.musfeat.architecture.BaseFragment
import com.example.musfeat.presentation.SignUpPresenter
import com.example.musfeat.view.signIn.SignInFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject


@AndroidEntryPoint
class SignUpFragment : BaseFragment(R.layout.fragment_sign_up), SignUpView {

    @Inject
    lateinit var registrationPresenter: SignUpPresenter

    private val presenter: SignUpPresenter by moxyPresenter { registrationPresenter }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.toolbar?.title = getString(R.string.registration_title)
        setListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {

        etNameReg.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_name_24, 0, 0, 0)
        etSurnameReg.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_name_24, 0, 0, 0)
        etEmailReg.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_email_24, 0, 0, 0)
        etPasswordReg.setCompoundDrawablesRelativeWithIntrinsicBounds(
            R.drawable.ic_lock_24,
            0,
            0,
            0
        )
        etPasswordRegRepeat.setCompoundDrawablesRelativeWithIntrinsicBounds(
            R.drawable.ic_lock_24,
            0,
            0,
            0
        )

        val twName: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val resources = context!!.resources
                val theme = context!!.theme
                if (p0?.isNotEmpty()!!) {
                    var drawable =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_name_24, theme)
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(
                        drawable,
                        ResourcesCompat.getColor(resources, R.color.colorDarkBlue, theme)
                    )
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    etNameReg.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                } else {
                    etNameReg.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_name_24,
                        0,
                        0,
                        0
                    )
                    var drawable =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_name_24, theme)
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(
                        drawable!!,
                        ResourcesCompat.getColor(resources, R.color.colorDefault, theme)
                    )
                    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
                    etNameReg.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    etNameReg.setCompoundDrawablesWithIntrinsicBounds(
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_name_24, theme),
                        null, null, null
                    )
                }
            }
        }
        val twSurname: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val resources = context!!.resources
                val theme = context!!.theme
                if (p0?.isNotEmpty()!!) {
                    var drawable =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_name_24, theme)
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(
                        drawable!!,
                        ResourcesCompat.getColor(resources, R.color.colorDarkBlue, theme)
                    )
                    DrawableCompat.setTintMode(drawable!!, PorterDuff.Mode.SRC_IN)
                    etSurnameReg.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                } else {
                    etSurnameReg.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_name_24,
                        0,
                        0,
                        0
                    )
                    var drawable =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_name_24, theme)
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(
                        drawable!!,
                        ResourcesCompat.getColor(resources, R.color.colorDefault, theme)
                    )
                    DrawableCompat.setTintMode(drawable!!, PorterDuff.Mode.SRC_IN)
                    etSurnameReg.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    etSurnameReg.setCompoundDrawablesWithIntrinsicBounds(
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_name_24, theme),
                        null, null, null
                    )
                }
            }
        }
        val twEmail: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val resources = context!!.resources
                val theme = context!!.theme
                if (p0?.isNotEmpty()!! && presenter.isEmailValid(p0.toString())) {
                    var drawable =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_email_24, theme)
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(
                        drawable!!,
                        ResourcesCompat.getColor(resources, R.color.colorDarkBlue, theme)
                    )
                    DrawableCompat.setTintMode(drawable!!, PorterDuff.Mode.SRC_IN)
                    etEmailReg.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                } else {
                    etEmailReg.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_email_24,
                        0,
                        0,
                        0
                    )
                    var drawable =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_email_24, theme)
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(
                        drawable!!,
                        ResourcesCompat.getColor(resources, R.color.colorDefault, theme)
                    )
                    DrawableCompat.setTintMode(drawable!!, PorterDuff.Mode.SRC_IN)
                    etEmailReg.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    etEmailReg.setCompoundDrawablesWithIntrinsicBounds(
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_email_24, theme),
                        null, null, null
                    )
                }
            }
        }
        val twPassword: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val resources = context!!.resources
                val theme = context!!.theme
                if (presenter.isPasswordValid(p0.toString())) {
                    var drawable =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_lock_24, theme)
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(
                        drawable!!,
                        ResourcesCompat.getColor(resources, R.color.colorDarkBlue, theme)
                    )
                    DrawableCompat.setTintMode(drawable!!, PorterDuff.Mode.SRC_IN)
                    etPasswordReg.setCompoundDrawablesWithIntrinsicBounds(
                        drawable,
                        null,
                        null,
                        null
                    )
                } else {
                    etPasswordReg.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_lock_24,
                        0,
                        0,
                        0
                    )
                    var drawable =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_lock_24, theme)
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(
                        drawable!!,
                        ResourcesCompat.getColor(resources, R.color.colorDefault, theme)
                    )
                    DrawableCompat.setTintMode(drawable!!, PorterDuff.Mode.SRC_IN)
                    etPasswordReg.setCompoundDrawablesWithIntrinsicBounds(
                        drawable,
                        null,
                        null,
                        null
                    )
                    etPasswordReg.setCompoundDrawablesWithIntrinsicBounds(
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_lock_24, theme),
                        null, null, null
                    )
                }
            }
        }
        val twPasswordRepeat: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val resources = context!!.resources
                val theme = context!!.theme
                if (presenter.isPasswordValid(p0.toString()) && isPasswordsEquals()) {
                    var drawable =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_lock_24, theme)
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(
                        drawable!!,
                        ResourcesCompat.getColor(resources, R.color.colorDarkBlue, theme)
                    )
                    DrawableCompat.setTintMode(drawable!!, PorterDuff.Mode.SRC_IN)
                    etPasswordRegRepeat.setCompoundDrawablesWithIntrinsicBounds(
                        drawable,
                        null,
                        null,
                        null
                    )
                } else {
                    etPasswordRegRepeat.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_lock_24,
                        0,
                        0,
                        0
                    )
                    var drawable =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_lock_24, theme)
                    drawable = DrawableCompat.wrap(drawable!!)
                    DrawableCompat.setTint(
                        drawable!!,
                        ResourcesCompat.getColor(resources, R.color.colorDefault, theme)
                    )
                    DrawableCompat.setTintMode(drawable!!, PorterDuff.Mode.SRC_IN)
                    etPasswordRegRepeat.setCompoundDrawablesWithIntrinsicBounds(
                        drawable,
                        null,
                        null,
                        null
                    )
                    etPasswordRegRepeat.setCompoundDrawablesWithIntrinsicBounds(
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_lock_24, theme),
                        null, null, null
                    )
                }
            }
        }
        val twBtnCreateAccount: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                val mName: String = etNameReg.text.toString().trim()
                val mSurname: String = etSurnameReg.text.toString().trim()
                val mEmail: String = etEmailReg.text.toString().trim()
                val mPassword: String = etPasswordReg.text.toString().trim()
                val mPasswordReg: String = etPasswordRegRepeat.text.toString().trim()
                if (mName.isNotEmpty() && mSurname.isNotEmpty() && presenter.isEmailValid(mEmail) &&
                    presenter.isPasswordValid(mPassword) && presenter.isPasswordValid(mPasswordReg) &&
                    mPassword == mPasswordReg
                ) {
                    btnCreateAccount.setBackgroundResource(R.color.colorDarkBlue)
                } else {
                    btnCreateAccount.setBackgroundResource(R.color.colorWhiteBlueShade)
                }
            }
        }

        etNameReg.addTextChangedListener(twName)
        etSurnameReg.addTextChangedListener(twSurname)
        etEmailReg.addTextChangedListener(twEmail)
        etPasswordReg.addTextChangedListener(twPassword)
        etPasswordRegRepeat.addTextChangedListener(twPasswordRepeat)
        btnCreateAccount.addTextChangedListener(twBtnCreateAccount)

        onTouchListener(etNameReg)
        onTouchListener(etSurnameReg)
        onTouchListener(etEmailReg)
        onTouchListener(etPasswordReg)
        onTouchListener(etPasswordRegRepeat)

        btnCreateAccount.setOnClickListener {
            (activity as MainActivity).showProgressBar(true)
            presenter.checkData(
                etNameReg.text.toString().trim(),
                etSurnameReg.text.toString().trim(),
                etEmailReg.text.toString().trim(),
                etPasswordReg.text.toString().trim(),
                etPasswordRegRepeat.text.toString().trim()
            )
        }
    }

    fun isPasswordsEquals(): Boolean =
        etPasswordReg.text.toString() == etPasswordRegRepeat.text.toString()

    override fun showError(message: String) {
        (activity as MainActivity).showProgressBar(false)
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun toSignInFragment() {
        val resources = requireContext().resources
        val theme = requireContext().theme
        var drawable = DrawableCompat.wrap(
            ResourcesCompat.getDrawable(resources, R.drawable.ic_name_24, theme)!!
        )
        DrawableCompat.setTint(
            drawable!!,
            ResourcesCompat.getColor(resources, R.color.colorDefault, theme)
        )
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
        etNameReg.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        etSurnameReg.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

        drawable = DrawableCompat.wrap(
            ResourcesCompat.getDrawable(resources, R.drawable.ic_email_24, theme)!!
        )
        DrawableCompat.setTint(
            drawable!!,
            ResourcesCompat.getColor(resources, R.color.colorDefault, theme)
        )
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
        etEmailReg.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

        drawable = DrawableCompat.wrap(
            ResourcesCompat.getDrawable(resources, R.drawable.ic_email_24, theme)!!
        )
        DrawableCompat.setTint(
            drawable!!,
            ResourcesCompat.getColor(resources, R.color.colorDefault, theme)
        )
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
        etPasswordReg.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        etPasswordRegRepeat.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

        (activity as MainActivity).showProgressBar(false)
        Toast.makeText(
            requireContext(),
            getString(R.string.registration_success),
            Toast.LENGTH_LONG
        ).show()
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, SignInFragment())
            .commit()
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
}
