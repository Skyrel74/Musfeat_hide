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
import com.example.musfeat.data.MusicalInstrument
import com.example.musfeat.glide.GlideApp
import com.example.musfeat.presentation.ProfilePresenter
import com.example.musfeat.util.FirestoreUtil
import com.example.musfeat.util.StorageUtil
import com.example.musfeat.view.MainActivity
import com.example.musfeat.view.signIn.SignInFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wrapper.*
import kotlinx.android.synthetic.main.fragment_profile.*
import moxy.ktx.moxyPresenter
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile), ProfileView {

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
                val selectedImageBytes = outputStream.toByteArray()
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
        activity?.toolbar?.title = getString(R.string.profile_title)
        (activity as MainActivity).showNavView(true)
        (activity as MainActivity).showBackBtn(false)

        FirestoreUtil.getCurrentUser { user ->
            tilName?.editText?.setText(user.name)
            tilSurname?.editText?.setText(user.surname)
            tilDescription?.editText?.setText(user.description)
        }

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
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, SignInFragment())
                .commit()
        }
        if (savedInstanceState == null)
            parentFragmentManager.beginTransaction()
                .replace(R.id.preferences, SettingFragment())
                .commit()
    }

    override fun onDestroyView() {
        val musicalInstrument = mutableListOf<MusicalInstrument>()
        val preference = PreferenceManager.getDefaultSharedPreferences(this.context)
        val isDrummer = preference.getBoolean("isDrummer", false)
        val isVocalist = preference.getBoolean("isVocalist", false)
        val isGuitarPlayer = preference.getBoolean("isGuitarPlayer", false)

        if (isDrummer) musicalInstrument.add(MusicalInstrument.DRUM)
        if (isVocalist) musicalInstrument.add(MusicalInstrument.VOCAL)
        if (isGuitarPlayer) musicalInstrument.add(MusicalInstrument.GUITAR)
        if (musicalInstrument.size == 0) musicalInstrument.add(MusicalInstrument.NONE)

        val name = tilName?.editText?.text.toString()
        val surname = tilSurname?.editText?.text.toString()
        val description = tilDescription?.editText?.text.toString()

        FirestoreUtil.updateCurrentUser(
            name = name,
            surname = surname,
            description = description,
            musicalInstrument = musicalInstrument
        )

        super.onDestroyView()
    }
}

class SettingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_profile, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preference = PreferenceManager.getDefaultSharedPreferences(this.context)
        FirestoreUtil.getCurrentUser { user ->
            if (user.musicalInstrument.contains(MusicalInstrument.NONE))
                preference
                    .edit()
                    .putBoolean("isDrummer", false)
                    .putBoolean("isVocalist", false)
                    .putBoolean("isGuitarPlayer", false)
                    .apply()
            if (user.musicalInstrument.contains(MusicalInstrument.DRUM))
                preference
                    .edit()
                    .putBoolean("isDrummer", true)
                    .apply()
            if (user.musicalInstrument.contains(MusicalInstrument.VOCAL))
                preference
                    .edit()
                    .putBoolean("isVocalist", true)
                    .apply()
            if (user.musicalInstrument.contains(MusicalInstrument.GUITAR))
                preference
                    .edit()
                    .putBoolean("isGuitarPlayer", true)
                    .apply()
        }

        val isGuitarPlayerPref: SwitchPreferenceCompat? = findPreference("isGuitarPlayer")
        isGuitarPlayerPref!!.setIcon(R.drawable.ic_eye_24)
    }
}
