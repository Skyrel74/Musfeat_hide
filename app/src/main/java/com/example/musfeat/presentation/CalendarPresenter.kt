package com.example.musfeat.presentation

import android.util.Log
import com.example.musfeat.data.Event
import com.example.musfeat.domain.GetEventsUseCase
import com.example.musfeat.view.calendar.CalendarView
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import moxy.presenterScope
import javax.inject.Inject

class CalendarPresenter @Inject constructor(private val getEventsUseCase: GetEventsUseCase) :
    MvpPresenter<CalendarView>() {

    private lateinit var events: List<Event>

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.showLoading(isShow = true)

        presenterScope.launch(CoroutineExceptionHandler { _, error ->
            Log.e("TAG", error.message, error)
            viewState.showLoading(isShow = false)
        }) {
            events = getEventsUseCase()
            viewState.setEvents(events)
            viewState.showLoading(isShow = false)
        }
    }
}
