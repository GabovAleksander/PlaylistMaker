package com.practicum.playlistmaker.new_playlist.ui.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmaker.new_playlist.domain.models.PermissionsResultState
import com.practicum.playlistmaker.new_playlist.domain.models.Playlist
import com.practicum.playlistmaker.new_playlist.ui.viewmodels.BtnCreateState
import com.practicum.playlistmaker.new_playlist.ui.viewmodels.NewPlaylistViewModel
import com.practicum.playlistmaker.new_playlist.ui.viewmodels.ScreenState
import com.practicum.playlistmaker.utils.setImage
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class NewPlaylistFragment : Fragment() {
    private lateinit var binding: FragmentNewPlaylistBinding
    private val viewModel by viewModel<NewPlaylistViewModel>()
    private var playlist: Playlist? = null
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPlaylist()

        binding.playlistName.doOnTextChanged { s: CharSequence?, _, _, _ ->
            binding.buttonCreate.isEnabled = !s.isNullOrEmpty()
        }

        binding.buttonCreate.setOnClickListener {
            if (!viewModel.isClickable)
                return@setOnClickListener
            viewModel.onBtnClick()

            val name = binding.playlistName.text.toString()
            val description = binding.playlistDescription.text.toString()
            if (playlist != null) {
                viewModel.updatePlaylist(
                    playlist!!.playlistId,
                    name = name,
                    description = description,
                    imageUri = imageUri
                ) {
                    findNavController().popBackStack()
                }
            } else {
                viewModel.createPlaylist(
                    name = name,
                    description = description,
                    imageUri = imageUri
                ) {
                    Toast.makeText(
                        requireContext(),
                        String.format(
                            resources.getText(R.string.created).toString(),
                            name
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().popBackStack()
                }

            }
        }

        initToolbar()

        initPickMediaRegister()

        initBackPressed()

    }

    @Suppress("DEPRECATION")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        playlist = arguments?.getSerializable(PLAYLIST) as Playlist?

    }

    private fun initPlaylist() {
        playlist?.let {
            binding.toolbar.title = getString(R.string.edit_title)
            binding.buttonCreate.text = getString(R.string.save_playlist)
            binding.playlistName.setText(it.name)
            binding.playlistDescription.setText(it.description)
            it.cover?.let { imageName ->
                binding.playlistCoverImage.setImageURI(
                    File(
                        File(
                            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            PLAYLISTS_IMAGES
                        ), imageName
                    ).toUri()
                )
            }
            binding.buttonCreate.isEnabled = true
        }
    }

    private fun initBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (checkInput()) {
                    showDialog()
                } else {
                    findNavController().popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun initPickMediaRegister() {
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.playlistCoverImage.setImageURI(uri)
                    imageUri = uri
                }
            }
        binding.playlistCoverImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun checkInput(): Boolean {
        playlist?.let {
            return false
        }
        return (
                imageUri != null
                        || binding.playlistName.text.toString().isNotEmpty()
                        || binding.playlistDescription.text.toString().isNotEmpty()
                )
    }

    private fun initToolbar() {
        binding.toolbar.setOnClickListener {
            if (checkInput()) {
                showDialog()
            } else {
                findNavController().popBackStack()
            }
        }
    }



    private fun goBack() {
        findNavController().navigateUp()
    }



    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext(),R.style.CustomMaterialDialog)
            .setTitle(getString(R.string.title_playlist_dialog))
            .setMessage(getString(R.string.message_playlist_dialog))
            .setNeutralButton(getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.complete)) { _, _ -> goBack() }
            .show()
    }


    companion object {
        private const val  PLAYLIST="playlist"
        private const val  PLAYLISTS_IMAGES="playlist_images"
    }
}