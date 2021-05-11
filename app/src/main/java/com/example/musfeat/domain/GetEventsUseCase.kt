package com.example.musfeat.domain

import com.example.musfeat.data.Event
import com.example.musfeat.di.EventApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(private val eventApi: EventApi) {

    suspend operator fun invoke(): List<Event> = withContext(Dispatchers.IO) {
        eventApi.getEvents().run {
            results?.mapNotNull { event ->
                Event(
                    name = eventNamePattern(event.title) ?: return@mapNotNull null,
                    date = event.dates?.get(0)?.date.toString(),
                    participantCount = event.favoritesCount.toString(),
                    eventImageView = event.images?.get(0)?.image ?: return@mapNotNull null,
                    description = event.description ?: return@mapNotNull null,
                    url = event.siteUrl ?: "https://kudago.com/ekb/"
                )
            } ?: emptyList()
        }
    }

    private fun eventNamePattern(title: String?): String? {
        return when (title) {
            null -> null
            else -> {
                if (title.length > 10)
                    title.substring(0, 11) + "..."
                else
                    title
            }
        }
    }
}
