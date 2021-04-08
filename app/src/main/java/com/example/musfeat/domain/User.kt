package com.example.musfeat.domain

data class User(
    val uid: String,
    val name: String,
    val surname: String,
    val email: String,
    val musicalInstrument: List<MusicalInstrument>
)