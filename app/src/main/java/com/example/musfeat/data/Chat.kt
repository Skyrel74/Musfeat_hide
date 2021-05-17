package com.example.musfeat.data

data class Chat(
    val channelId: String = "",
    val name: String = "",
    val usersIds: List<String> = emptyList(),
    val messagesIds: List<String>? = null
)
