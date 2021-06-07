package com.example.musfeat.view.chat

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musfeat.R
import com.example.musfeat.data.User
import com.example.musfeat.presentation.ChatPresenter
import com.example.musfeat.util.FirestoreUtil
import com.example.musfeat.view.MainActivity
import com.example.musfeat.view.message.MessageFragment
import com.example.musfeat.view.recyclerview.item.ChatItem
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*
import kotlinx.android.synthetic.main.fragment_chat.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : MvpAppCompatFragment(R.layout.fragment_chat), ChatView {

    private lateinit var chatListenerRegistration: ListenerRegistration
    private lateinit var chatSection: Section
    private var shouldInitRecyclerView: Boolean = true

    @Inject
    lateinit var chatPresenter: ChatPresenter
    private val presenter: ChatPresenter by moxyPresenter { chatPresenter }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.toolbar?.title = getString(R.string.chat_title)
        (activity as MainActivity).showNavView(true)
        (activity as MainActivity).showBackBtn(false)

        chatListenerRegistration =
            FirestoreUtil.addChatsListener(this.requireActivity(), this::updateRecyclerView)
    }

    override fun updateRecyclerView(items: List<Item>) {

        fun init() {
            rvChats.apply {
                layoutManager = LinearLayoutManager(this@ChatFragment.context)
                adapter = GroupAdapter<ViewHolder>().apply {
                    chatSection = Section(items)
                    add(chatSection)
                    setOnItemClickListener(presenter.onItemClick())
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

    override fun toMessageFragment(secondUser: User, item: ChatItem) {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.container, MessageFragment.newInstance(
                    secondUser.name,
                    secondUser.uid,
                    item.chat.channelId
                )
            )
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        FirestoreUtil.removeListener(chatListenerRegistration)
        shouldInitRecyclerView = true
    }
}
