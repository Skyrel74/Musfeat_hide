package com.example.musfeat.presentation


import android.util.Log
import com.example.musfeat.architecture.BasePresenter
import com.example.musfeat.data.Event
import com.example.musfeat.domain.GetDatabaseUseCase
import com.example.musfeat.domain.GetEventsUseCase
import com.example.musfeat.view.calendar.CalendarView
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import moxy.presenterScope

import javax.inject.Inject

class CalendarPresenter @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val getDatabaseUseCase: GetDatabaseUseCase
) : BasePresenter<CalendarView>() {
    private lateinit var events: List<Event>


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showLoading(isShow = true)
        presenterScope.launch(CoroutineExceptionHandler { _, error ->
            Log.e("tag", error.message, error)
            viewState.showLoading(isShow = false)
        }) {

            //TODO check if there is network to select source
//            events = getEventsUseCase()
            viewState.setEvents(events)
            viewState.showLoading(isShow = false)
        }
    }

    //fixme
    suspend fun selectDatasource(isConnectionAvailable: Boolean) {
        events =
            if (isConnectionAvailable) getEventsUseCase().also { getDatabaseUseCase(it) } else getDatabaseUseCase()
    }
}
