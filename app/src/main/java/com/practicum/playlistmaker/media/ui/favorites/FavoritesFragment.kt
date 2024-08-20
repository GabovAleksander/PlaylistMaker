package com.practicum.playlistmaker.media.ui.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.media.ui.viewmodels.FavoritesFragmentViewModel
import com.practicum.playlistmaker.media.ui.viewmodels.FavoritesState
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.ui.adapters.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel by viewModel<FavoritesFragmentViewModel>()
    private val tracksAdapter = TrackAdapter {
        clickOnTrack(it)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeContentState().observe(viewLifecycleOwner) {
            render(it)
        }
        initAdapter()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoritesTracks()
    }

    private fun clickOnTrack(track: Track) {

        if (!viewModel.isClickable) return
        viewModel.addToHistory(track)
        viewModel.onTrackClick()
        findNavController().navigate(R.id.action_libraryFragment_to_audioPlayer)
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun render(state: FavoritesState) {
        when (state) {
            is FavoritesState.Empty -> {
                binding.favoritesList.visibility = View.GONE
                binding.placeholderNothingWasFound.visibility = View.VISIBLE
            }

            is FavoritesState.FavoritesTracks -> {
                tracksAdapter.notifyDataSetChanged()
                tracksAdapter.tracks = state.tracks
                binding.placeholderNothingWasFound.visibility = View.GONE
                binding.favoritesList.visibility = View.VISIBLE
            }
        }
    }

    private fun initAdapter() {
        binding.favoritesList.adapter = tracksAdapter
    }


    companion object {
        fun newInstance() = FavoritesFragment()
    }
}