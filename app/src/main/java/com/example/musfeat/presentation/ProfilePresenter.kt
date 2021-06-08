package com.example.musfeat.presentation

import android.content.SharedPreferences
import com.example.musfeat.data.MusicalInstrument
import com.example.musfeat.util.FirestoreUtil
import com.example.musfeat.view.profile.ProfileView
import com.google.firebase.auth.FirebaseAuth
import moxy.MvpPresenter
import javax.inject.Inject

class ProfilePresenter @Inject constructor() : MvpPresenter<ProfileView>() {

    fun saveData(
        preference: SharedPreferences,
        name: String,
        surname: String,
        description: String
    ) {
        if (FirebaseAuth.getInstance().currentUser != null) {

            val musicalInstrument = mutableListOf<MusicalInstrument>()
            val isDrummer = preference.getBoolean("isDrummer", false)
            val isVocalist = preference.getBoolean("isVocalist", false)
            val isGuitarPlayer = preference.getBoolean("isGuitarPlayer", false)

            if (isDrummer) musicalInstrument.add(MusicalInstrument.DRUM)
            if (isVocalist) musicalInstrument.add(MusicalInstrument.VOCAL)
            if (isGuitarPlayer) musicalInstrument.add(MusicalInstrument.GUITAR)
            if (musicalInstrument.size == 0) musicalInstrument.add(MusicalInstrument.NONE)

            val searchSettings = mutableListOf<MusicalInstrument>()
            val isLookingForDrummer = preference.getBoolean("isLookingForDrummer", false)
            val isLookingForVocalist = preference.getBoolean("isLookingForVocalist", false)
            val isLookingForGuitarPlayer = preference.getBoolean("isLookingForGuitarPlayer", false)

            if (isLookingForDrummer) searchSettings.add(MusicalInstrument.DRUM)
            if (isLookingForVocalist) searchSettings.add(MusicalInstrument.VOCAL)
            if (isLookingForGuitarPlayer) searchSettings.add(MusicalInstrument.GUITAR)
            if (searchSettings.size == 0) searchSettings.add(MusicalInstrument.NONE)

            FirestoreUtil.updateCurrentUser(
                name = name,
                surname = surname,
                description = description,
                musicalInstrument = musicalInstrument
            )

            FirestoreUtil.setSearchSettings(searchSettings)
        }
    }
}
