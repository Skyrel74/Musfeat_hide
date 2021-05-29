package com.example.musfeat.domain

import com.example.musfeat.EventDatabase
import com.example.musfeat.data.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetDatabaseUseCase @Inject constructor(private val eventDatabase: EventDatabase) {

    suspend operator fun invoke(): List<Event> = withContext(Dispatchers.IO){
        eventDatabase.eventDao().getAll()
    }

    suspend operator fun invoke(events:List<Event>){
        withContext(Dispatchers.IO){
            eventDatabase.eventDao().insertAllByList(events)
        }
    }
}