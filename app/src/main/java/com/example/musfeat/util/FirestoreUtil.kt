package com.example.musfeat.util

import android.content.Context
import android.util.Log
import com.example.musfeat.data.*
import com.example.musfeat.view.recyclerview.item.ChatItem
import com.example.musfeat.view.recyclerview.item.ImageMessageItem
import com.example.musfeat.view.recyclerview.item.TextMessageItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item
import java.util.*

object FirestoreUtil {

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document(
            "users/${FirebaseAuth.getInstance().uid
                ?: throw NullPointerException("UID is null.")}"
        )
    private val chatChannelsCollectionRef = firestoreInstance.collection("chatChannels")

    fun initCurrentUserIfFirstTime(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                val newUser = User(
                    FirebaseAuth.getInstance().currentUser?.displayName ?: "",
                    "",
                    "",
                    "",
                    "",
                    listOf(MusicalInstrument.NONE),
                    null,
                    ""
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
        musicalInstrument: List<MusicalInstrument> = listOf(), description: String = "",
        userPicturePath: String? = null, registrationTokens: String? = null
    ) {
        val userFieldMap = mutableMapOf<String, Any>()
        if (uid.isNotBlank()) userFieldMap["uid"] = uid
        if (name.isNotBlank()) userFieldMap["name"] = name
        if (surname.isNotBlank()) userFieldMap["surname"] = surname
        if (email.isNotBlank()) userFieldMap["email"] = email
        if (description.isNotBlank()) userFieldMap["description"] = description
        if (musicalInstrument.isNotEmpty()) userFieldMap["musicalInstrument"] = musicalInstrument
        if (userPicturePath != null) userFieldMap["userPicturePath"] = userPicturePath
        if (registrationTokens != null) userFieldMap["registrationTokens"] = registrationTokens
        currentUserDocRef.update(userFieldMap)
    }

    fun getSearchSettings(onComplete: (List<MusicalInstrument>) -> Unit) {
        currentUserDocRef
            .collection("extra")
            .document("search")
            .get()
            .addOnSuccessListener {
                if (it.exists())
                    onComplete(it.data?.get("searchSettings") as List<MusicalInstrument>)
            }
    }

    fun setSearchSettings(searchSettings: List<MusicalInstrument>) {
        currentUserDocRef
            .collection("extra")
            .document("search")
            .set(mapOf("searchSettings" to searchSettings))
    }

    fun getCurrentUser(onComplete: (User) -> Unit) {
        currentUserDocRef.get().addOnSuccessListener {
            onComplete(it.toObject(User::class.java)!!)
        }
    }

    fun getUserByUid(uid: String, onComplete: (User) -> Unit) {
        firestoreInstance.document("users/$uid").get().addOnSuccessListener {
            onComplete(it.toObject(User::class.java)!!)
        }
    }

    fun addUsersListener(onListen: (List<User>) -> Unit): ListenerRegistration {
        return firestoreInstance.collection("users/")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("Firestore", "Users listener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val users = mutableListOf<User>()
                querySnapshot?.documents?.forEach {
                    if (it.id != FirebaseAuth.getInstance().currentUser?.uid) {
                        val user: User = it.toObject(User::class.java)!!
                        users.add(user)
                    }
                }
                onListen(users)
            }
    }

    fun removeListener(registration: ListenerRegistration) = registration.remove()

    fun addChatsListener(context: Context, onListen: (List<Item>) -> Unit): ListenerRegistration {
        return currentUserDocRef.collection("extra").document("chats")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "Chat listener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                if (querySnapshot != null && querySnapshot.exists()) {
                    var channelIds: List<String>? = mutableListOf()
                    channelIds = querySnapshot?.data?.get("channelIds") as List<String>

                    val items = mutableListOf<Item>()
                    channelIds.forEach { channelId ->
                        chatChannelsCollectionRef.document(channelId).get()
                            .addOnSuccessListener { ds ->
                                val messagesIds = mutableListOf<String>()
                                chatChannelsCollectionRef.document(channelId).collection("messages")
                                    .get().addOnSuccessListener {
                                        it.documents.forEach { ds ->
                                            messagesIds.add(ds.id)
                                        }
                                        val chat = Chat(
                                            channelId,
                                            ds.data?.get("name").toString(),
                                            ds.data?.get("userIds") as MutableList<String>,
                                            messagesIds
                                        )
                                        items.add(ChatItem(chat, context))
                                        onListen(items)
                                    }
                            }
                    }
                }
            }
    }

    fun getOrCreateChatChannel(
        channelId: String,
        otherUserId: String,
        onComplete: (channelId: String) -> Unit
    ) {
        currentUserDocRef.collection("extra")
            .document("chats").get().addOnSuccessListener {
                if (it.data?.get("channelIds").toString().contains(channelId)) {
                    onComplete(channelId)
                    return@addOnSuccessListener
                }

                val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

                val newChannel = chatChannelsCollectionRef.document()
                newChannel.set(ChatChannel(mutableListOf(currentUserId, otherUserId)))

                currentUserDocRef
                    .collection("extra")
                    .document("chats")
                    .get().addOnSuccessListener { ds ->
                        if (ds.exists())
                            currentUserDocRef
                                .collection("extra")
                                .document("chats")
                                .update("channelIds", FieldValue.arrayUnion(newChannel.id))
                        else
                            currentUserDocRef
                                .collection("extra")
                                .document("chats")
                                .set(mapOf("channelIds" to listOf(newChannel.id)))
                    }

                firestoreInstance.collection("users").document(otherUserId)
                    .collection("extra")
                    .document("chats")
                    .get().addOnSuccessListener { ds ->
                        if (ds.exists())
                            firestoreInstance.collection("users").document(otherUserId)
                                .collection("extra")
                                .document("chats")
                                .update("channelIds", FieldValue.arrayUnion(newChannel.id))
                        else
                            firestoreInstance.collection("users").document(otherUserId)
                                .collection("extra")
                                .document("chats")
                                .set(mapOf("channelIds" to listOf(newChannel.id)))
                    }

                onComplete(newChannel.id)
            }
    }

    fun addChatMessagesListener(
        channelId: String, context: Context,
        onListen: (List<Item>) -> Unit
    ): ListenerRegistration {
        return chatChannelsCollectionRef.document(channelId).collection("messages")
            .orderBy("time")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "ChatMessagesListener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<Item>()
                querySnapshot?.documents?.forEach {
                    if (it["messageType"].toString() == MessageType.TEXT)
                        items.add(TextMessageItem(it.toObject(TextMessage::class.java)!!, context))
                    else
                        items.add(
                            ImageMessageItem(
                                it.toObject(ImageMessage::class.java)!!,
                                context
                            )
                        )
                }
                onListen(items)
            }
    }

    fun sendMessage(message: Message, channelId: String) {
        chatChannelsCollectionRef.document(channelId)
            .collection("messages")
            .add(message)
    }

    fun getFCMRegistrationTokens(userId: String, onComplete: (token: String?) -> Unit) {
        getUserByUid(userId) {
            onComplete(it.registrationTokens)
        }
    }

    fun setFCMRegistrationTokens(registrationTokens: MutableList<String>) {
        currentUserDocRef.update(mapOf("registrationTokens" to registrationTokens))
    }

    fun getRandomUsers(onSuccess: (MutableList<User>) -> Unit) {
        firestoreInstance.collection("users")
            .get().addOnSuccessListener {
                onSuccess(it.toObjects(User::class.java).shuffled().toMutableList())
            }
    }

    fun getLikedUsers(userId: String, onComplete: (List<String>) -> Unit) {
        firestoreInstance.collection("users")
            .document(userId)
            .collection("extra")
            .document("liked")
            .get().addOnSuccessListener {
                if (it.exists())
                    onComplete(it?.data?.get("likedUsersIds") as List<String>)
                else
                    onComplete(emptyList())
            }
    }

    fun getDislikedUsers(userId: String, onComplete: (List<String>) -> Unit) {
        firestoreInstance.collection("users")
            .document(userId)
            .collection("extra")
            .document("disliked")
            .get().addOnSuccessListener {
                if (it.exists())
                    onComplete(it?.data?.get("dislikedUsersIds") as List<String>)
                else
                    onComplete(emptyList())
            }
    }

    fun setLikedUser(likedUser: User) {
        currentUserDocRef.collection("extra")
            .document("liked")
            .get().addOnSuccessListener { ds ->
                if (!ds.exists())
                    currentUserDocRef.collection("extra")
                        .document("liked")
                        .set(mapOf("likedUsersIds" to listOf(likedUser.uid)))
                else {
                    currentUserDocRef.collection("extra")
                        .document("liked")
                        .update("likedUsersIds", FieldValue.arrayUnion(likedUser.uid))
                }
            }
        getLikedUsers(likedUser.uid) {
            if (it.contains(FirebaseAuth.getInstance().currentUser!!.uid)) {
                getOrCreateChatChannel(UUID.randomUUID().toString(), likedUser.uid) {}
            }
        }
    }

    fun setDislikedUser(dislikedUser: User) {
        currentUserDocRef.collection("extra")
            .document("disliked")
            .get().addOnSuccessListener { ds ->
                if (!ds.exists())
                    currentUserDocRef.collection("extra")
                        .document("disliked")
                        .set(mapOf("dislikedUsersIds" to listOf(dislikedUser.uid)))
                else {
                    currentUserDocRef.collection("extra")
                        .document("disliked")
                        .update("dislikedUsersIds", FieldValue.arrayUnion(dislikedUser.uid))
                }
            }
    }
}
