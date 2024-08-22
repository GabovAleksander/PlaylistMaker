package com.practicum.playlistmaker.media.ui.playlist

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.media.ui.bottom_sheet.PlaylistMenuBottomSheet
import com.practicum.playlistmaker.media.ui.bottom_sheet.PlaylistsBottomSheet
import com.practicum.playlistmaker.media.ui.state.PlaylistState
import com.practicum.playlistmaker.media.ui.viewmodels.PlaylistViewModel
import com.practicum.playlistmaker.new_playlist.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.ui.adapters.TrackAdapter
import com.practicum.playlistmaker.utils.shareText
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private val viewModel: PlaylistViewModel by viewModel()

    private lateinit var binding: FragmentPlaylistBinding

    private lateinit var playlist: Playlist

    private val trackAdapter = TrackAdapter({ clickOnTrack(it) }, { longClickOnTrack(it) })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistState.PlaylistTracks -> {
                    showTracks(it.tracks)
                }

                is PlaylistState.PlaylistInfo -> {
                    playlist = it.playlist
                    showPlaylist()
                }
            }
        }

        initToolbar()

        initShareBtn()

        initMoreBtn()

        initAdapter()

    }

    override fun onResume() {
        super.onResume()
        viewModel.requestPlayListInfo(playlist.playlistId)
    }

    @Suppress("DEPRECATION")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        playlist = arguments?.getSerializable(PLAYLIST) as Playlist
    }

    private fun initToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initAdapter() {
        binding.tracksRecycler.adapter = trackAdapter
    }

    private fun initShareBtn() {
        binding.btnShare.setOnClickListener {
            if (viewModel.clickDebounce()) {
                if (trackAdapter.tracks.isNotEmpty()) {
                    shareText(createMessageForShare(), requireContext())
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.empty_track_list),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun initMoreBtn() {
        binding.btnMore.setOnClickListener {
            PlaylistMenuBottomSheet.newInstance(playlist, createMessageForShare())
                .show(childFragmentManager, "PlaylistsBottomSheet")
        }
    }

    private fun createMessageForShare(): String {
        var message =
            "${playlist.name}\n${playlist.description}\n" + binding.totalTracksCount.resources.getQuantityString(
                R.plurals.tracks,
                trackAdapter.tracks.size,
                trackAdapter.tracks.size
            ) + "\n"
        trackAdapter.tracks.forEachIndexed { index, track ->
            message += "\n ${index + 1}. ${track.artistName} - ${track.trackName} (" + SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(track.trackTimeMillis) + ")"
        }
        return message
    }

    private fun showPlaylist() {
        with(binding) {
            val filePath = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                PLAYLISTS_IMAGES
            )
            Glide
                .with(playlistCover)
                .load(playlist.cover?.let { imageName -> File(filePath, imageName) })
                .placeholder(R.drawable.icon_no_picture_big)
                .into(playlistCover)

            playlistName.text = playlist.name
            playlistName.isSelected = true

            if (playlist.description.isNotEmpty()) {
                playlistDescription.text = playlist.description
                playlistDescription.isSelected = true
                playlistDescription.visibility = View.VISIBLE
            } else {
                playlistDescription.visibility = View.GONE
            }
        }

        initBottomSheetBehavior()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showTracks(tracks: List<Track>) {
        with(binding) {
            if (tracks.isEmpty()) {
                ivPlaceholder.visibility = View.VISIBLE
                tvPlaceholder.visibility = View.VISIBLE
            }
            trackAdapter.tracks = tracks
            trackAdapter.notifyDataSetChanged()
            var totalDuration = 0L
            trackAdapter.tracks.forEach { track ->
                totalDuration += track.trackTimeMillis
            }
            totalDuration = TimeUnit.MILLISECONDS.toMinutes(totalDuration)

            totalTime.text = totalTime.resources.getQuantityString(
                R.plurals.minutes,
                totalDuration.toInt(),
                totalDuration
            )

            totalTracksCount.text = totalTracksCount.resources.getQuantityString(
                R.plurals.tracks,
                trackAdapter.tracks.size,
                trackAdapter.tracks.size
            )
        }
    }

    private fun initBottomSheetBehavior() {
        with(binding) {
            val bottomSheetBehavior = BottomSheetBehavior.from(tracksBottomSheet)
            bottomBlank.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        bottomSheetBehavior.peekHeight = bottomBlank.height
                        bottomBlank.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            )
        }
    }

    private fun clickOnTrack(track: Track) {
        if (viewModel.clickDebounce()) {
            findNavController().navigate(
                R.id.action_to_Player,
                Bundle().apply {
                    putSerializable(TRACK, track)
                }
            )
        }
    }

    private fun longClickOnTrack(track: Track) {
        MaterialAlertDialogBuilder(requireContext(), R.style.CustomMaterialDialog)
            .setTitle(resources.getText(R.string.delete_track))
            .setNegativeButton(resources.getText(R.string.no)) { _, _ ->
            }
            .setPositiveButton(resources.getText(R.string.yes)) { _, _ ->
                viewModel.deleteTrack(track.trackId, playlist.playlistId)
            }
            .show()
    }

    companion object{
        val TRACK="track"
        val PLAYLISTS_IMAGES="playlist_images"
        val PLAYLIST="playlist"
    }
}