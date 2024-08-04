package com.practicum.playlistmaker.media.ui.playlists

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.media.ui.adapters.PlaylistsAdapter
import com.practicum.playlistmaker.media.ui.viewmodels.PlaylistsScreenState
import com.practicum.playlistmaker.media.ui.viewmodels.PlaylistsViewModel
import com.practicum.playlistmaker.new_playlist.domain.models.Playlist
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistsBinding
    private val viewModel by viewModel<PlaylistsViewModel>()
    private val playlistsAdapter = PlaylistsAdapter {
        clickOnPlaylist()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.contentFlow.collect { screenState ->
                render(screenState)
            }
        }

        binding.buttonNewPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_libraryFragment_to_newPlaylistFragment
            )
        }
    }

    private fun render(state: PlaylistsScreenState) {
        when (state) {
            is PlaylistsScreenState.Content -> showContent(state.playlists)
            PlaylistsScreenState.Empty -> showPlaceholder()
        }
    }

    private fun showPlaceholder() {
        binding.apply {
            placeholderNoPlaylist.visibility = View.VISIBLE
            recyclerViewPlaylist.visibility = View.GONE
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(content: List<Playlist>) {

        binding.apply {
            placeholderNoPlaylist.visibility = View.GONE
            recyclerViewPlaylist.visibility = View.VISIBLE
        }

        playlistsAdapter.apply {
            playlists.clear()
            playlists.addAll(content)
            notifyDataSetChanged()
        }
    }

    private fun initAdapter() {
        binding.recyclerViewPlaylist.adapter = playlistsAdapter
        binding.recyclerViewPlaylist.addItemDecoration(PlaylistsOffsetItemDecoration(requireContext()))
    }

    private fun clickOnPlaylist() {
        if (!viewModel.isClickable) return
        viewModel.onPlaylistClick()
        Toast
            .makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT)
            .show()
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}