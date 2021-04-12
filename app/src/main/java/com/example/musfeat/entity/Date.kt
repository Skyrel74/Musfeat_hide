package com.example.napoleonitapp.entity


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Date(
    @SerialName("end")
    val end: Int?,
    @SerialName("end_date")
    val endDate: String?,
    @SerialName("end_time")
    val endTime: String?,
    @SerialName("is_continuous")
    val isContinuous: Boolean?,
    @SerialName("is_endless")
    val isEndless: Boolean?,
    @SerialName("is_startless")
    val isStartless: Boolean?,
    @SerialName("start")
    val start: Int?,
    @SerialName("start_date")
    val date: String?,
    @SerialName("start_time")
    val startTime: String?,
    @SerialName("use_place_schedule")
    val usePlaceSchedule: Boolean?
)