package com.example.musfeat

import android.os.Bundle
import com.example.musfeat.architecture.BaseActivity
import com.example.musfeat.view.LoginFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wrapper)
        setStartupFragment(savedInstanceState)
    }

    // Переопределить для MVP
    private fun setStartupFragment(savedInstanceState: Bundle?) {

        toolbar.title = getString(R.string.login_title)

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .add(R.id.container, LoginFragment())
                .commit()
    }
}