package com.example.musfeat.view.message

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musfeat.AppConstants
import com.example.musfeat.R
import com.example.musfeat.data.ImageMessage
import com.example.musfeat.data.TextMessage
import com.example.musfeat.data.User
import com.example.musfeat.presentation.MessagePresenter
import com.example.musfeat.service.MyFirebaseMessagingService
import com.example.musfeat.util.FirestoreUtil
import com.example.musfeat.util.StorageUtil
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
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MessageFragment : MvpAppCompatFragment(R.layout.fragment_message), MessageView {

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val selectedImagePath = data?.data
                val selectedImageBmp = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val src: ImageDecoder.Source = ImageDecoder.createSource(
                        requireActivity().contentResolver,
                        selectedImagePath!!
                    )
                    ImageDecoder.decodeBitmap(src)
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        selectedImagePath
                    )
                }
                val outputStream = ByteArrayOutputStream()
                selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                val selectedImageBytes = outputStream.toByteArray()
                StorageUtil.uploadMessageImage(selectedImageBytes) { imagePath ->
                    val messageToSend =
                        ImageMessage(
                            imagePath, Calendar.getInstance().time,
                            FirebaseAuth.getInstance().currentUser!!.uid
                        )
                    FirestoreUtil.sendMessage(messageToSend, channelId)
                }
            }
        }

    private var uName: String? = null
    private lateinit var uId: String
    private lateinit var channelId: String
    private lateinit var currentUser: User

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

    private lateinit var messagesListenerRegistration: ListenerRegistration
    private lateinit var messageSection: Section
    private var shouldInitRecyclerView = true

    @Inject
    lateinit var messagePresenter: MessagePresenter
    private val presenter: MessagePresenter by moxyPresenter { messagePresenter }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirestoreUtil.getCurrentUser {
            currentUser = it
        }

        uId = requireArguments().getSerializable(AppConstants.USER_ID) as String
        channelId = requireArguments().getSerializable(AppConstants.CHANNEL_ID) as String
        uName = requireArguments().getSerializable(AppConstants.USER_NAME) as String

        activity?.toolbar?.title = uName
        (activity as MainActivity).showNavView(false)
        (activity as MainActivity).showBackBtn(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSettingsFragment()
    }

    override fun setSettingsFragment() {

        FirestoreUtil.getOrCreateChatChannel(channelId, uId) { channelId ->
            messagesListenerRegistration =
                FirestoreUtil.addChatMessagesListener(
                    channelId,
                    this.requireContext(),
                    this::updateRecyclerView
                )

            ivSend.setOnClickListener {
                val messageToSend = TextMessage(
                    etMessage.text.toString(), Calendar.getInstance().time,
                    FirebaseAuth.getInstance().currentUser!!.uid
                )
                etMessage.setText("")
                FirestoreUtil.sendMessage(messageToSend, channelId)
                sendNotification(messageToSend.text)
            }

            fabSendImage.setOnClickListener {
                val intent: Intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                }
                resultLauncher.launch(intent)
                sendNotification("Вы получили изображение.")
            }
        }

    }


    override fun updateRecyclerView(items: List<Item>) {
        fun init() {
            rvMessages.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = GroupAdapter<ViewHolder>().apply {
                    messageSection = Section(items)
                    this.add(messageSection)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = messageSection.update(items)

        if (shouldInitRecyclerView)
            init()
        else
            updateItems()

        if (rvMessages?.adapter?.itemCount != null)
            rvMessages.scrollToPosition(rvMessages.adapter!!.itemCount - 1)
    }

    private fun sendNotification(message: String) {
        val to = JSONObject()
        val data = JSONObject()

        data.put("title", currentUser.name)
        data.put("body", currentUser.name)
        data.put("message", message)
        data.put("uName", currentUser.name)
        data.put("uId", currentUser.uid)
        data.put("channelId", channelId)
        data.put("notificationFlag", AppConstants.NOTIFICATION_FLAG_CHAT)

        FirestoreUtil.getFCMRegistrationTokens(uId) {
            to.put("to", it)
            to.put("data", data)
            MyFirebaseMessagingService.sendNotification(requireContext(), to)
        }
    }
}
