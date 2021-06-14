package com.example.musfeat.view.calendar

import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import com.example.musfeat.R
import com.example.musfeat.data.Event
import com.example.musfeat.presentation.CalendarPresenter
import com.example.musfeat.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.coroutines.runBlocking
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import org.jetbrains.anko.connectivityManager
import javax.inject.Inject

@AndroidEntryPoint
class CalendarFragment : MvpAppCompatFragment(R.layout.fragment_calendar), CalendarView {

    @Inject
    lateinit var calendarPresenter: CalendarPresenter
    private val presenter: CalendarPresenter by moxyPresenter { calendarPresenter }
    private var calendarAdapter: CalendarAdapter? = null
    private var preferences: SharedPreferences? = null
    private var oldTimestamp: Long? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.toolbar?.title = getString(R.string.calendar_title)
        (activity as MainActivity).showNavView(true)
        (activity as MainActivity).showBackBtn(false)
        preferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        oldTimestamp = preferences?.getLong("calendar_cache_timestamp", 0)

        runBlocking {
            presenter.selectDatasource(
                isConnectionAvailable = isInternetAvailable(),
                currentTimestamp = System.currentTimeMillis(),
                oldTimestamp = oldTimestamp!!
            )
        }
        with(rvCalendar) {
            val spanCount =
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 3 else 5
            layoutManager = GridLayoutManager(context, spanCount, GridLayoutManager.VERTICAL, false)
            adapter = CalendarAdapter {}.also {
                calendarAdapter = it
            }
        }
    }

    override fun setTimestamp(timestamp: Long) {
        preferences?.edit()?.putLong("calendar_cache_timestamp", timestamp)?.apply()
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = requireContext().connectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }

    override fun showLoading(isShow: Boolean) {
        (activity as MainActivity).pbLoading.isVisible = isShow
    }

    override fun setEvents(events: List<Event>) {
        calendarAdapter?.submitList(events)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        calendarAdapter = null
    }
}
