package com.example.musfeat.view.calendar

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.musfeat.R
import com.example.musfeat.architecture.BaseFragment
import com.example.musfeat.data.Event
import com.example.musfeat.presentation.CalendarPresenter
import com.example.musfeat.view.MainActivity
import com.example.musfeat.view.calendar.adapter.CalendarAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject

@AndroidEntryPoint
class CalendarFragment : BaseFragment(R.layout.fragment_calendar), CalendarView {

    @Inject
    lateinit var calendarPresenter: CalendarPresenter
    private val presenter: CalendarPresenter by moxyPresenter { calendarPresenter }
    private var calendarAdapter: CalendarAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.toolbar?.title = getString(R.string.calendar_title)
        (activity as MainActivity).showNavView(true)
        (activity as MainActivity).showBackBtn(false)

        with(rvCalendar) {
            val spanCount =
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 3 else 5
            layoutManager = GridLayoutManager(context, spanCount, GridLayoutManager.VERTICAL, false)
            adapter = CalendarAdapter(onEventClick = {
                // TODO(Сделать подробнее о событиях)
            }).also {
                calendarAdapter = it
            }
        }
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
