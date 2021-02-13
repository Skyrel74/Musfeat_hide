package com.example.musfeat.architecture

import android.widget.ListAdapter
import moxy.MvpAppCompatFragment

open class BaseFragment(layout: Int) : MvpAppCompatFragment(layout), BaseView {

    private var adapter: ListAdapter? = null

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }
}