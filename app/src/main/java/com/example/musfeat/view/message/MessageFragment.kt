package com.example.musfeat.view.message

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musfeat.AppConstants
import com.example.musfeat.R
import com.example.musfeat.architecture.BaseFragment
import com.example.musfeat.data.MessageType
import com.example.musfeat.data.TextMessage
import com.example.musfeat.util.FirestoreUtil
import com.example.musfeat.view.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*
import kotlinx.android.synthetic.main.fragment_message.*
import java.util.*

@AndroidEntryPoint
class MessageFragment : BaseFragment(R.layout.fragment_message), MessageView {

    private lateinit var messagesListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true
    private lateinit var messageSection: Section

    private var uName: String? = null
    private var uId: String? = null
    private var channelId: String? = null

    companion object {

        fun newInstance(uName: String, uId: String, channelId: String): MessageFragment {
            val fragment = MessageFragment()
            val args = Bundle()
            args.putSerializable(AppConstants.USER_NAME, uName)
            args.putSerializable(AppConstants.USER_ID, uId)
            args.putSerializable(AppConstants.CHANNEL_ID, channelId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            uName = requireArguments().getSerializable(AppConstants.USER_NAME) as String
            uId = requireArguments().getSerializable(AppConstants.USER_ID) as String
            channelId = requireArguments().getSerializable(AppConstants.CHANNEL_ID) as String
            FirestoreUtil.getOrCreateChatChannel(channelId!!, uId!!) { channelId ->
                messagesListenerRegistration =
                    FirestoreUtil.addChatMessagesListener(
                        channelId,
                        this.requireContext(),
                        this::updateRecyclerView
                    )

                ivSend.setOnClickListener {
                    val messageToSend = TextMessage(
                        etMessage.text.toString(), Calendar.getInstance().time,
                        FirebaseAuth.getInstance().currentUser!!.uid, MessageType.TEXT
                    )
                    etMessage.setText("")
                    FirestoreUtil.sendMessage(messageToSend, channelId)
                }

                fabSendImage.setOnClickListener {
                    TODO("Send image messages")
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSettingsFragment(savedInstanceState)
    }

    override fun setSettingsFragment(savedInstanceState: Bundle?) {
        activity?.toolbar?.title = uName
        (activity as MainActivity).showNavView(false)
        (activity as MainActivity).showBackBtn(true)
        if (savedInstanceState == null)
            parentFragmentManager.beginTransaction()
                .replace(R.id.preferences, MessageFragment.newInstance(uName!!, uId!!, channelId!!))
                .commit()
    }


    override fun updateRecyclerView(messages: List<Item>) {
        fun init() {
            rvMessages.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = GroupAdapter<ViewHolder>().apply {
                    messageSection = Section(messages)
                    this.add(messageSection)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = messageSection.update(messages)

        if (shouldInitRecyclerView)
            init()
        else
            updateItems()
        if (rvMessages?.adapter?.itemCount != null)
            rvMessages.scrollToPosition(rvMessages.adapter!!.itemCount - 1)
    }
}
