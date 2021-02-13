package com.example.musfeat

import android.os.Bundle
import com.example.musfeat.architecture.BaseActivity
import com.example.musfeat.view.signIn.SignInFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wrapper)
        setSupportActionBar(toolbar)
        setStartupFragment(savedInstanceState)
    }

    // Переопределить для MVP
    private fun setStartupFragment(savedInstanceState: Bundle?) {


        if (savedInstanceState == null) {
            toolbar.title = getString(R.string.login_title)

            supportFragmentManager.beginTransaction()
                .add(R.id.container, SignInFragment())
                .commit()
        }
    }
}