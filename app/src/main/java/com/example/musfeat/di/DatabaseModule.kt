package com.example.musfeat.di

import android.content.Context
import com.example.musfeat.EventDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideEventDatabase(@ApplicationContext context: Context) =
        EventDatabase.getInstance(context)
}