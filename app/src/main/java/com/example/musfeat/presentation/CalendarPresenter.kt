package com.example.musfeat.presentation

import android.util.Log
import com.example.musfeat.architecture.BasePresenter
import com.example.musfeat.data.Event
import com.example.musfeat.domain.GetEventsUseCase
import com.example.musfeat.view.calendar.CalendarView
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import moxy.presenterScope
import javax.inject.Inject


class CalendarPresenter @Inject constructor(private val getEventsUseCase: GetEventsUseCase) :
    BasePresenter<CalendarView>() {


    lateinit var events: List<Event>

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.showLoading(isShow = true)

        presenterScope.launch(CoroutineExceptionHandler { context, throwable ->
            viewState.showLoading(isShow = false)
            Log.e("tag", throwable.message, throwable)
        }) {
            events = getEventsUseCase()
            viewState.setEvents(events)
            viewState.showLoading(isShow = false)

        }
    }
}