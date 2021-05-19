package com.example.musfeat.view.map

import android.os.Bundle
import android.view.View
import com.example.musfeat.R
import com.example.musfeat.architecture.BaseFragment
import com.example.musfeat.architecture.BaseView
import com.example.musfeat.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*

@AndroidEntryPoint
class MapFragment : BaseFragment(R.layout.fragment_map), BaseView {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.toolbar?.title = getString(R.string.map_title)
        (activity as MainActivity).showNavView(true)
        (activity as MainActivity).showBackBtn(false)
    }
}