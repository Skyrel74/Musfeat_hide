package com.example.musfeat.view.chat

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musfeat.R
import com.example.musfeat.architecture.BaseFragment
import com.example.musfeat.util.FirestoreUtil
import com.example.musfeat.view.message.MessageFragment
import com.example.musfeat.view.recyclerview.item.ChatItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*
import kotlinx.android.synthetic.main.fragment_chat.*

@AndroidEntryPoint
class ChatFragment : BaseFragment(R.layout.fragment_chat) {

    private lateinit var chatListenerRegistration: ListenerRegistration

    private var shouldInitRecyclerView: Boolean = true

    private lateinit var chatSection: Section

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatListenerRegistration =
            FirestoreUtil.addChatsListener(this.requireActivity(), this::updateRecyclerView)

        activity?.toolbar?.title = getString(R.string.chat_title)
    }

    private fun updateRecyclerView(items: List<Item>) {

        fun init() {
            rvChats.apply {
                layoutManager = LinearLayoutManager(this@ChatFragment.context)
                adapter = GroupAdapter<ViewHolder>().apply {
                    chatSection = Section(items)
                    add(chatSection)
                    setOnItemClickListener(onItemClick)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = chatSection.update(items)

        if (shouldInitRecyclerView)
            init()
        else
            updateItems()
    }

    private val onItemClick = OnItemClickListener { item, view ->
        if (item is ChatItem) {
            val secondId: String =
                item.chat.usersIds.filter { it != FirebaseAuth.getInstance().currentUser!!.uid }[0]
            FirestoreUtil.getUserByUid(secondId) { secondUser ->
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.container, MessageFragment.newInstance(
                            secondUser.name,
                            secondUser.uid
                        )
                    )
                    .commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        FirestoreUtil.removeListener(chatListenerRegistration)
        shouldInitRecyclerView = true
    }
}
