package com.example.musfeat.view.calendar

import com.example.musfeat.data.Event
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface CalendarView : MvpView {

    @AddToEndSingle
    fun showLoading(isShow: Boolean)

    @AddToEndSingle
    fun setEvents(events: List<Event>)
}
