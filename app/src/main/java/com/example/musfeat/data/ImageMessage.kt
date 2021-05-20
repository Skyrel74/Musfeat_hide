package com.example.musfeat.data

import java.util.*

data class ImageMessage(
    val imagePath: String,
    override val time: Date,
    override val senderId: String,
    override val messageType: String = MessageType.IMAGE
) : Message {
    constructor() : this("", Date(0), "", "")
}
