package com.example.musfeat.presentation


import android.util.Log
import com.example.musfeat.AppConstants.CALENDAR_CACHE_TIME
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
            viewState.setEvents(events)
            viewState.showLoading(isShow = false)
        }
    }

    suspend fun selectDatasource(
        isConnectionAvailable: Boolean,
        currentTimestamp: Long,
        oldTimestamp: Long
    ) {
        val timestampDiff = currentTimestamp - oldTimestamp
        events = when {
            !isConnectionAvailable -> getDatabaseUseCase()
            isConnectionAvailable && (timestampDiff < CALENDAR_CACHE_TIME) -> getDatabaseUseCase()
            isConnectionAvailable && (timestampDiff >= CALENDAR_CACHE_TIME) -> getEventsUseCase().also {
                getDatabaseUseCase(it)
                viewState.setTimestamp(currentTimestamp)
            }
            else -> getDatabaseUseCase()
        }
    }
}
