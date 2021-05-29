package com.example.musfeat.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@Entity(tableName = "events")
data class Event(
    val name: String, val date: String, val participantCount: String,
    val eventImageView: String, val description: String,@PrimaryKey val url: String
) : Parcelable
