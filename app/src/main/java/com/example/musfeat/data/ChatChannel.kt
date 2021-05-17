package com.example.musfeat.data

data class ChatChannel(val userIds: MutableList<String>) {
    constructor() : this(mutableListOf())
}
