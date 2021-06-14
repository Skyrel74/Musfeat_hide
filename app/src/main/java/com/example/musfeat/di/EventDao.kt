package com.example.musfeat.di

import androidx.room.*
import com.example.musfeat.data.Event

@Dao
interface EventDao {

    /**
     * Insert all events to database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg events: Event)

    /**
     * Insert all events to database by list
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllByList(list: List<Event>)

    /**
     * Delete event from database
     */
    @Delete
    fun deleteEvent(event: Event)

    /**
     * Get all events from database
     */
    @Query("SELECT * FROM events")
    fun getAll(): List<Event>
}
