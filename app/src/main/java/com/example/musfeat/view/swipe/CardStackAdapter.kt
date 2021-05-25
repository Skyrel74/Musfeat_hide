package com.example.musfeat.view.swipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.musfeat.R
import com.example.musfeat.data.User
import com.example.musfeat.glide.GlideApp
import com.example.musfeat.util.StorageUtil
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_swipe.*

class CardStackAdapter :
    androidx.recyclerview.widget.ListAdapter<User, CardStackAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }) {

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_swipe, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        if (user.userPicturePath != null)
            GlideApp.with(holder.containerView.context)
                .load(StorageUtil.pathToReference(user.userPicturePath))
                .placeholder(R.drawable.img)
                .into(holder.ivSwipe)
        else
            holder.ivSwipe.setImageResource(R.drawable.img)
        holder.tvSwipeDescription.text = user.description
    }
}