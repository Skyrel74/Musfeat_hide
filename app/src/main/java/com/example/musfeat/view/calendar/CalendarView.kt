package com.example.musfeat.view.calendar

import com.example.musfeat.architecture.BaseView
import com.example.musfeat.data.Event
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface CalendarView : BaseView {

    @AddToEndSingle
    fun showLoading(isShow: Boolean)

    @AddToEndSingle
    fun setEvents(events: List<Event>)

    @OneExecution
    fun setTimestamp(timestamp: Long)
}
