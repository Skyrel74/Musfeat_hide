package com.example.musfeat.view.profile

import android.os.Bundle
import android.preference.PreferenceFragment
import android.util.Log
import android.view.View
import androidx.preference.*
import com.example.musfeat.R
import com.example.musfeat.architecture.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*

@AndroidEntryPoint
class ProfileFragment : PreferenceFragmentCompat(){

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_profile,null)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.toolbar?.title = getString(R.string.profile_title)
        //sample code to get
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        val isGuitarPlayer = sharedPreferences.getBoolean("isGuitarPlayer",false)
        val isGuitarPlayerPref: SwitchPreferenceCompat? = findPreference("isGuitarPlayer")
        isGuitarPlayerPref!!.setIcon(R.drawable.ic_eye_24)
    }

}
