package com.example.musfeat.view.recyclerview.item

import android.view.Gravity
import android.widget.FrameLayout
import com.example.musfeat.R
import com.example.musfeat.data.Message
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_text_message.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.wrapContent
import java.text.SimpleDateFormat
import java.util.*


abstract class MessageItem(private val message: Message) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        setTimeText(viewHolder)
        setMessageRootGravity(viewHolder)
    }

    private fun setTimeText(viewHolder: ViewHolder) {
        val dateFormat = SimpleDateFormat
            .getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT, Locale("ru"))
        viewHolder.tvMessageTime.text = dateFormat.format(message.time!!)
    }

    private fun setMessageRootGravity(viewHolder: ViewHolder) {
        if (message.senderId == FirebaseAuth.getInstance().currentUser?.uid) {
            viewHolder.messageRoot.apply {
                backgroundResource = R.drawable.rect_round_white
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.END)
                this.layoutParams = lParams
            }
        } else {
            viewHolder.messageRoot.apply {
                backgroundResource = R.drawable.rect_round_primary_color
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.START)
                this.layoutParams = lParams
            }
        }
    }
}
