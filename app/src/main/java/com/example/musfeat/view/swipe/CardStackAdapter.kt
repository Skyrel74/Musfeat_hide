package com.example.musfeat.view.swipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musfeat.R
import com.example.musfeat.data.User
import com.example.musfeat.glide.GlideApp
import com.example.musfeat.util.StorageUtil
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.item_swipe.*

class CardStackAdapter : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    private var dataSet: MutableList<User> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_swipe, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(dataSet[position])

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(user: User) {
            if (user.userPicturePath != null)
                GlideApp.with(this.containerView.context)
                    .load(StorageUtil.pathToReference(user.userPicturePath))
                    .placeholder(R.drawable.img)
                    .into(profileImg)
            else
                ivSwipe.setImageResource(R.drawable.img)
            tvSwipeDescription.text = user.description
        }
    }

    override fun getItemCount(): Int = dataSet.size

    fun setData(dataSet: MutableList<User>) {
        this.dataSet = dataSet
    }

    fun removeItem(user: User) {
        val position = dataSet.indexOf(user)
        if (position != -1)
            dataSet.removeAt(position)
        notifyItemRemoved(position)
    }
}