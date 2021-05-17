package com.example.musfeat.data

data class Chat(
    val chatId: String = "",
    val name: String = "",
    val usersIds: List<String> = emptyList(),
    val messagesIds: List<String>? = null
)
