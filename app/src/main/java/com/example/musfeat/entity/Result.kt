package com.example.napoleonitapp.entity


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    @SerialName("body_text")
    val bodyText: String?,
    @SerialName("dates")
    val dates: List<Date>?,
    @SerialName("description")
    val description: String?,
    @SerialName("favorites_count")
    val favoritesCount: Int?,
    @SerialName("images")
    val images: List<Image>?,
    @SerialName("site_url")
    val siteUrl: String?,
    @SerialName("title")
    val title: String?
)