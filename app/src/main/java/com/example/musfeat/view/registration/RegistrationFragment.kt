package com.example.musfeat.view.registration

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.musfeat.R
import com.example.musfeat.architecture.BaseFragment
import com.example.musfeat.presentation.RegistrationPresenter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*
import kotlinx.android.synthetic.main.fragment_registration.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject


@AndroidEntryPoint
class RegistrationFragment : BaseFragment(R.layout.fragment_registration), RegistrationView {


    companion object {

        @JvmStatic
        fun newInstance() = RegistrationFragment().apply {
            arguments = Bundle().apply { }
        }
    }

    @Inject
    lateinit var registrationPresenter: RegistrationPresenter

    private val presenter: RegistrationPresenter by moxyPresenter { registrationPresenter }

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
                if (p0?.length != 0) {
                    editView(R.color.colorDarkBlue, R.drawable.ic_name_24, etNameReg)
                } else if (p0.isEmpty()) {
                    editView(R.color.colorDefault, R.drawable.ic_name_24, etNameReg)
                }
            }
        })

        etSurnameReg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length != 0) {
                    editView(R.color.colorDarkBlue, R.drawable.ic_name_24, etSurnameReg)
                } else if (p0.isEmpty()) {
                    etSurnameReg.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_email_24, 0, 0, 0
                    )
                    editView(R.color.colorDefault, R.drawable.ic_name_24, etSurnameReg)
                }
            }
        })


        etEmailReg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length != 0) {
                    editView(R.color.colorDarkBlue, R.drawable.ic_email_24, etEmailReg)
                } else if (p0.isEmpty()) {
                    editView(R.color.colorDefault, R.drawable.ic_email_24, etEmailReg)
                }
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