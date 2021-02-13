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
import androidx.core.view.isVisible
import com.example.musfeat.R
import com.example.musfeat.architecture.BaseFragment
import com.example.musfeat.presentation.SignUpPresenter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject


@AndroidEntryPoint
class SignUpFragment : BaseFragment(R.layout.fragment_sign_up), SignUpView {


    companion object {

        @JvmStatic
        fun newInstance() = SignUpFragment().apply {
            arguments = Bundle().apply { }
        }
    }

    @Inject
    lateinit var registrationPresenter: SignUpPresenter

    private val presenter: SignUpPresenter by moxyPresenter { registrationPresenter }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.toolbar?.title = getString(R.string.registration_title)
        initListeners()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListeners() {

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



        etNameReg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                presenter.checkName(p0.toString().trim())

            }
        })

        etSurnameReg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                presenter.checkSurname(p0.toString().trim())
            }
        })


        etEmailReg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                presenter.checkEmail(p0.toString().trim())
            }
        })

        val twPasswordEquals: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val password = etPasswordReg.text.toString().trim()
                val passwordRepeat = etPasswordRegRepeat.text.toString().trim()
                presenter.checkPasswordEquality(password, passwordRepeat)
            }

        }

        etPasswordReg.addTextChangedListener(twPasswordEquals)
        etPasswordRegRepeat.addTextChangedListener(twPasswordEquals)

        onTouchListener(etNameReg)
        onTouchListener(etSurnameReg)
        onTouchListener(etEmailReg)
        onTouchListener(etPasswordReg)
        onTouchListener(etPasswordRegRepeat)

        btnCreateAccount.setOnClickListener {
            pbLoading.isVisible = true
            btnCreateAccount.setBackgroundResource(R.color.colorWhiteBlueShade)
            presenter.checkData(
                etNameReg.text.toString().trim(),
                etSurnameReg.text.toString().trim(),
                etEmailReg.text.toString().trim(),
                etPasswordReg.text.toString().trim(),
                etPasswordRegRepeat.text.toString().trim(),
                cbGuitar.isChecked,
                cbVocal.isChecked,
                cbDrums.isChecked
            )
        }
    }

    override fun onPasswordNotEqual() {
        editView(R.color.colorRed, R.drawable.ic_lock_24, etPasswordReg)
        editView(R.color.colorRed, R.drawable.ic_lock_24, etPasswordRegRepeat)
    }

    override fun onPasswordEqual() {
        editView(R.color.colorDarkBlue, R.drawable.ic_lock_24, etPasswordReg)
        editView(R.color.colorDarkBlue, R.drawable.ic_lock_24, etPasswordRegRepeat)
    }

    override fun onPasswordEmpty() {
        editView(R.color.colorDefault, R.drawable.ic_lock_24, etPasswordReg)
        editView(R.color.colorDefault, R.drawable.ic_lock_24, etPasswordRegRepeat)
    }

    override fun onNameEmpty() {
        editView(R.color.colorDefault, R.drawable.ic_name_24, etNameReg)
    }

    override fun onNameHas() {
        editView(R.color.colorDarkBlue, R.drawable.ic_name_24, etNameReg)
    }

    override fun onSurnameEmpty() {
        editView(R.color.colorDefault, R.drawable.ic_name_24, etSurnameReg)
    }

    override fun onSurnameHas() {
        editView(R.color.colorDarkBlue, R.drawable.ic_name_24, etSurnameReg)
    }

    override fun onEmailEmpty() {
        editView(R.color.colorDefault, R.drawable.ic_email_24, etEmailReg)
    }

    override fun onEmailHas() {
        editView(R.color.colorDarkBlue, R.drawable.ic_email_24, etEmailReg)
    }

    override fun showError(message: String) {
        pbLoading.isVisible = false
        btnCreateAccount.setBackgroundResource(R.color.colorDarkBlue)
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun toMenuActivity() {
        pbLoading.isVisible = false
        btnCreateAccount.setBackgroundResource(R.color.colorDarkBlue)
        Toast.makeText(requireContext(), "Успешный вход", Toast.LENGTH_LONG).show()
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

    private fun editView(colorId: Int, imgId: Int, editTextId: EditText) {
        val resources = requireContext().resources
        val theme = requireContext().theme
        var drawable =
            ResourcesCompat.getDrawable(resources, imgId, theme)
        drawable = DrawableCompat.wrap(drawable!!)
        DrawableCompat.setTint(
            drawable!!,
            ResourcesCompat.getColor(resources, colorId, theme)
        )
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
        editTextId.setCompoundDrawablesWithIntrinsicBounds(
            drawable,
            null,
            null,
            null
        )
        if (editTextId.text.toString().trim()
                .isEmpty()
        ) editTextId.setCompoundDrawablesWithIntrinsicBounds(
            ResourcesCompat.getDrawable(resources, imgId, theme),
            null, null, null
        )
        else editTextId.setCompoundDrawablesWithIntrinsicBounds(
            ResourcesCompat.getDrawable(resources, imgId, theme),
            null,
            ResourcesCompat.getDrawable(resources, R.drawable.ic_cancel_24, theme),
            null
        )
    }


}