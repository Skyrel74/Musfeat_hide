package com.example.musfeat.data

data class User(
    val uid: String = "",
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val musicalInstrument: List<MusicalInstrument> = listOf(MusicalInstrument.NONE),
    val userPicturePath: String? = null
)
