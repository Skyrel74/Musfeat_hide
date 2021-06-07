package com.example.musfeat.view.profile

import android.os.Bundle
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEnd
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface ProfileView : MvpView {

    @AddToEnd
    fun setSettingsFragment(savedInstanceState: Bundle?)
}
