package com.example.musfeat.util

import com.example.musfeat.data.MusicalInstrument
import com.example.musfeat.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreUtil {

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document(
            "users/${FirebaseAuth.getInstance().uid
                ?: throw NullPointerException("UID is null.")}"
        )

    fun initCurrentUserIfFirstTime(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                val newUser = User(
                    FirebaseAuth.getInstance().currentUser?.displayName ?: "",
                    "",
                    "",
                    "",
                    listOf(MusicalInstrument.NONE),
                    null
                )
                currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()
                }
            } else {
                onComplete()
            }
        }
    }

    fun updateCurrentUser(
        uid: String = "", name: String = "", surname: String = "", email: String = "",
        musicalInstrument: List<MusicalInstrument> = listOf(),
        userPicturePath: String? = null
    ) {
        val userFieldMap = mutableMapOf<String, Any>()
        if (uid.isNotBlank()) userFieldMap["uid"] = uid
        if (name.isNotBlank()) userFieldMap["name"] = name
        if (surname.isNotBlank()) userFieldMap["surname"] = surname
        if (email.isNotBlank()) userFieldMap["email"] = email
        if (musicalInstrument.isNotEmpty()) userFieldMap["musicalInstrument"] = musicalInstrument
        if (userPicturePath != null) userFieldMap["userPicturePath"] = userPicturePath
        currentUserDocRef.update(userFieldMap)
    }

    fun getCurrentUser(onComplete: (User) -> Unit) {
        currentUserDocRef.get().addOnSuccessListener {
            onComplete(it.toObject(User::class.java)!!)
        }
    }
}
