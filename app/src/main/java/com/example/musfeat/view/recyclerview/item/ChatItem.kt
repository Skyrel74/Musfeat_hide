package com.example.musfeat.view.recyclerview.item

import android.content.Context
import com.example.musfeat.R
import com.example.musfeat.data.Chat
import com.example.musfeat.glide.GlideApp
import com.example.musfeat.util.FirestoreUtil
import com.example.musfeat.util.StorageUtil
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_chat.*

class ChatItem(
    val chat: Chat,
    private val context: Context
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        if (chat.usersIds.size == 2) {
            val secondUserId: String =
                chat.usersIds.filter { it != FirebaseAuth.getInstance().currentUser?.uid }[0]
            FirestoreUtil.getUserByUid(secondUserId) { user ->
                var title = "${user.name} ${user.surname}"
                if (title.length > 16)
                    title = title.substring(0, 15) + "..."
                viewHolder.tvChat.text = title
                viewHolder.tvMsg.text = title
                if (user.userPicturePath != null)
                    GlideApp.with(context)
                        .load(StorageUtil.pathToReference(user.userPicturePath))
                        .placeholder(R.drawable.img)
                        .into(viewHolder.ivChat)
            }
        }
        // TODO(Доделать множественный чат)
    }

    override fun getLayout(): Int = R.layout.item_chat
}
