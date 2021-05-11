package com.example.musfeat.view.profile

import android.os.Bundle
import com.example.musfeat.architecture.BaseView
import moxy.viewstate.strategy.alias.AddToEnd

interface ProfileView : BaseView {

    @AddToEnd
    fun setSettingsFragment(savedInstanceState: Bundle?)
}
