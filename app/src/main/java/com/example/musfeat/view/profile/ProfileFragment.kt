package com.example.musfeat.view.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.example.musfeat.R
import com.example.musfeat.architecture.BaseFragment
import com.example.musfeat.glide.GlideApp
import com.example.musfeat.presentation.ProfilePresenter
import com.example.musfeat.util.FirestoreUtil
import com.example.musfeat.util.StorageUtil
import com.example.musfeat.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*
import kotlinx.android.synthetic.main.fragment_profile.*
import moxy.ktx.moxyPresenter
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile), ProfileView {

    private lateinit var selectedImageBytes: ByteArray
    private var pictureJustChanged: Boolean = false

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
                selectedImageBytes = outputStream.toByteArray()
                StorageUtil.uploadProfilePhoto(selectedImageBytes) { imagePath ->
                    FirestoreUtil.updateCurrentUser(userPicturePath = imagePath)
                }

                GlideApp.with(this)
                    .load(selectedImageBytes)
                    .into(profileImg)
                pictureJustChanged = true
            }
        }

    @Inject
    lateinit var profilePresenter: ProfilePresenter
    private val presenter: ProfilePresenter by moxyPresenter { profilePresenter }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileImg.setOnClickListener {
            val intent: Intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
            }
            resultLauncher.launch(intent)
        }
        setSettingsFragment(savedInstanceState)
    }

    override fun setSettingsFragment(savedInstanceState: Bundle?) {
        activity?.toolbar?.title = getString(R.string.profile_title)
        (activity as MainActivity).showProgressBar(true)
        FirestoreUtil.getCurrentUser { user ->
            if (this@ProfileFragment.isVisible) {
                if (!pictureJustChanged && user.userPicturePath != null)
                    GlideApp.with(this)
                        .load(StorageUtil.pathToReference(user.userPicturePath))
                        .placeholder(R.drawable.img)
                        .into(profileImg)
            }
        }
        (activity as MainActivity).showProgressBar(false)
        if (savedInstanceState == null)
            parentFragmentManager.beginTransaction()
                .replace(R.id.preferences, SettingFragment())
                .commit()
    }
}

class SettingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_profile, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //sample code to get
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        val isGuitarPlayer = sharedPreferences.getBoolean("isGuitarPlayer", false)
        val isGuitarPlayerPref: SwitchPreferenceCompat? = findPreference("isGuitarPlayer")
        isGuitarPlayerPref!!.setIcon(R.drawable.ic_eye_24)
    }
}
