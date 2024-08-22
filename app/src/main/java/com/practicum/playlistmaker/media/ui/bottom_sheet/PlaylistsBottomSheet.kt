package com.practicum.playlistmaker.media.ui.bottom_sheet

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.R.id.design_bottom_sheet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.BottomSheetPlaylistsBinding
import com.practicum.playlistmaker.media.ui.adapters.BottomSheetAdapter
import com.practicum.playlistmaker.media.ui.adapters.PlaylistsAdapter
import com.practicum.playlistmaker.media.ui.adapters.PlaylistsViewHolder
import com.practicum.playlistmaker.media.ui.viewmodels.BottomSheetState
import com.practicum.playlistmaker.media.ui.viewmodels.BottomSheetViewModel
import com.practicum.playlistmaker.media.ui.viewmodels.PlaylistsState
import com.practicum.playlistmaker.new_playlist.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsBottomSheet(val track: Track) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetPlaylistsBinding
    private val viewModel by viewModel<BottomSheetViewModel>()

    private val playlistsAdapter = object : PlaylistsAdapter(
        clickListener = {
            clickOnPlaylist(it)
        }
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
            return PlaylistsViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_bottom_sheet, parent, false)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observePlaylistsState().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistsState.Empty -> binding.playlistsRecycler.visibility = View.GONE

                is PlaylistsState.Playlists -> {
                    playlistsAdapter.notifyDataSetChanged()
                    playlistsAdapter.playlists = it.playlists
                    binding.playlistsRecycler.visibility = View.VISIBLE
                }

                is PlaylistsState.AddTrackResult -> {
                    if (it.isAdded) {
                        showToast(getString(R.string.added, it.playlistName))
                        dismiss()
                    } else {
                        showToast(getString(R.string.already_added, it.playlistName))
                    }
                }
            }
        }

        initBtnCreate()
        initAdapter()
    }

    private fun initAdapter() {
        binding.playlistsRecycler.adapter = playlistsAdapter
    }

    private fun initBtnCreate() {
        binding.createPlaylistBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_to_new_playlist
            )
        }
    }

    private fun clickOnPlaylist(playlist: Playlist) {
        if (!viewModel.isClickable) return
        viewModel.onPlaylistClicked()
        viewModel.processResult(track, playlist)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()
    }

    private fun showToast(additionalMessage: String) {
        Toast.makeText(requireContext(), additionalMessage, Toast.LENGTH_LONG).show()
    }

    companion object {
        val TRACK="TRACK"
        val MESSAGE_DURATION_MILLIS=2000;
        fun createArgs(track: Track): Bundle = bundleOf(
            TRACK to Json.encodeToString(track)
        )
    }
}