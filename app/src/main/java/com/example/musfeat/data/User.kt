package com.example.musfeat.data

data class User(
    val uid: String,
    val name: String,
    val surname: String,
    val email: String,
    val description: String,
    val musicalInstrument: List<MusicalInstrument>,
    val userPicturePath: String?,
    val registrationTokens: String?
) {
    constructor() : this(
        "", "", "", "", "",
        listOf(MusicalInstrument.NONE), null, null
    )
}
