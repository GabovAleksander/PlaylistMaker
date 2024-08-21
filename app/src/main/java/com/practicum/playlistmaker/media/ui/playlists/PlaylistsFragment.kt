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
import com.practicum.playlistmaker.media.ui.adapters.PlaylistsViewHolder
import com.practicum.playlistmaker.media.ui.viewmodels.PlaylistsScreenState
import com.practicum.playlistmaker.media.ui.viewmodels.PlaylistsViewModel
import com.practicum.playlistmaker.new_playlist.domain.models.Playlist
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistsBinding
    private val viewModel by viewModel<PlaylistsViewModel>()
    private val playlistsAdapter = object : PlaylistsAdapter(
        clickListener = {
            clickOnPlaylist(it)
        }
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
            return PlaylistsViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_playlist, parent, false)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistsScreenState.Empty -> {
                    binding.recyclerViewPlaylist.visibility = View.GONE
                    binding.placeholderNoPlaylist.visibility = View.VISIBLE
                }

                is PlaylistsScreenState.Content -> {
                    playlistsAdapter.notifyDataSetChanged()
                    playlistsAdapter.playlists = it.playlists
                    binding.placeholderNoPlaylist.visibility = View.GONE
                    binding.recyclerViewPlaylist.visibility = View.VISIBLE
                    binding.recyclerViewPlaylist.smoothScrollToPosition(0)
                }
            }
        }

        initAdapter()

        initBtnNewPlaylist()
    }

    override fun onResume() {
        super.onResume()
        viewModel.updatePlaylists()
    }


    private fun initAdapter() {
        binding.recyclerViewPlaylist.adapter = playlistsAdapter
    }

    private fun initBtnNewPlaylist() {
        binding.buttonNewPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_to_new_playlist
            )
        }
    }

    private fun clickOnPlaylist(playlist: Playlist) {
        findNavController().navigate(
            R.id.action_to_PlaylistFragment,
            Bundle().apply {
                putSerializable(PLAYLIST, playlist)
            }
        )
    }

    companion object {
        private const val PLAYLIST="playlist"
        fun newInstance() = PlaylistsFragment()
    }
}