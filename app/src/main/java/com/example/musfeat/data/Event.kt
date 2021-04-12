package com.example.musfeat.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class Event(val name: String, val date: String, val participantCount: String,
                 val eventImageView: String, val description: String, val url: String) : Parcelable
