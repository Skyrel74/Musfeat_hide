package com.example.musfeat.util

import android.content.Context
import android.util.Log
import com.example.musfeat.data.*
import com.example.musfeat.view.recyclerview.item.ChatItem
import com.example.musfeat.view.recyclerview.item.ImageMessageItem
import com.example.musfeat.view.recyclerview.item.TextMessageItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item

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
        return currentUserDocRef.collection("extra")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "Chat listener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                var channelIds: List<String>? = mutableListOf()
                querySnapshot?.documents?.forEach {
                    channelIds = it.data?.get("channelIds") as MutableList<String>?
                }

                val items = mutableListOf<Item>()
                channelIds?.forEach { channelId ->
                    chatChannelsCollectionRef.document(channelId).get()
                        .addOnSuccessListener { ds ->
                            val messagesIds = mutableListOf<String>()
                            chatChannelsCollectionRef.document(channelId).collection("messages")
                                .get().addOnSuccessListener {
                                    it.documents.forEach { ds ->
                                        messagesIds.add(ds.id)
                                    }
                                    val chat = Chat(
                                        channelId, ds.data?.get("name").toString(),
                                        ds.data?.get("userIds") as List<String>, messagesIds
                                    )
                                    items.add(ChatItem(chat, context))
                                    onListen(items)
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
                        var data = ds.data?.get("channelIds").toString()
                        data = data.substring(1, data.length - 1)
                        val channelIds = mutableListOf(data)
                        channelIds.add(newChannel.id)
                        currentUserDocRef
                            .collection("extra")
                            .document("chats")
                            .set(mapOf("channelIds" to channelIds))
                    }

                firestoreInstance.collection("users").document(otherUserId)
                    .collection("extra")
                    .document("chats")
                    .get().addOnSuccessListener { ds ->
                        var data = ds.data?.get("channelIds").toString()
                        data = data.substring(1, data.length - 1)
                        val channelIds = mutableListOf(data)
                        channelIds.add(newChannel.id)
                        firestoreInstance.collection("users").document(otherUserId)
                            .collection("extra")
                            .document("chats")
                            .set(mapOf("channelIds" to channelIds))
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
}
