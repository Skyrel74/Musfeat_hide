package com.example.musfeat.di

import androidx.room.*
import com.example.musfeat.data.Event

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg events:Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllByList(list:List<Event>)

    @Delete
    fun deleteEvent(event:Event)


    @Query("SELECT * FROM event")
    fun getAll() :List<Event>
}