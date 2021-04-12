package com.example.napoleonitapp.entity


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Image(
    @SerialName("image")
    val image: String?,
    @SerialName("source")
    val source: Source?
)