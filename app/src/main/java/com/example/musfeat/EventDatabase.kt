package com.example.musfeat

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.musfeat.data.Event
import com.example.musfeat.di.EventDao

@Database(entities = [Event::class], version = 1, exportSchema = false)
abstract class EventDatabase:RoomDatabase() {
    abstract fun eventDao():EventDao

    companion object {
        @Volatile
        private var instance: EventDatabase? = null

        fun getInstance(context: Context): EventDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): EventDatabase {
            return Room.databaseBuilder(context, EventDatabase::class.java, "events.db").build()
        }
    }

}