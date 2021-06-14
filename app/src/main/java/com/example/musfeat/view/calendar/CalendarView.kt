package com.example.musfeat.view.calendar

import com.example.musfeat.data.Event
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

@AddToEndSingle
interface CalendarView : MvpView {

    /**
     * Show progress bar by [isShow] condition
     */
    @AddToEndSingle
    fun showLoading(isShow: Boolean)

    /**
     * Set items to recyclerview by [events] list
     */
    @AddToEndSingle
    fun setEvents(events: List<Event>)

    /**
     * Set timestamp by [timestamp]
     */
    @OneExecution
    fun setTimestamp(timestamp: Long)
}
