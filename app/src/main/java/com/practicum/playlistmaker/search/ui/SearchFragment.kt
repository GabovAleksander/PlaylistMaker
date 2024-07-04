package com.practicum.playlistmaker.search.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.ui.adapters.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

const val TRACK_KEY = "track_key"

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModel<SearchViewModel>()

    private val trackListAdapter = TrackAdapter {
        clickOnTrack(it)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.apply {
            observeState().observe(viewLifecycleOwner) {
                render(it)
            }
        }

        initInput()

        initHistory()

    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Success -> {
                trackListAdapter.tracks = state.tracks
                showContent(Content.SEARCH_RESULT)
            }

            is SearchState.ShowHistory -> {
                trackListAdapter.tracks = state.tracks
                showContent(Content.TRACKS_HISTORY)
            }

            is SearchState.Error -> {
                binding.errMessage.text = state.message
                showContent(Content.ERROR)
            }

            is SearchState.NothingFound -> showContent(Content.NOT_FOUND)
            is SearchState.Loading -> showContent(Content.LOADING)
        }
    }


    private fun initInput() {

        binding.editTextSearch.doOnTextChanged { s: CharSequence?, _, _, _ ->
            binding.clearIcon.visibility = clearButtonVisibility(s)
            if (binding.editTextSearch.hasFocus() && s.toString().isNotEmpty()) {
                showContent(Content.SEARCH_RESULT)
            }
            viewModel.searchDebounce(binding.editTextSearch.text.toString())
        }

        binding.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.getTracks(binding.editTextSearch.text.toString())
            }
            false
        }

        binding.errUpdateButton.setOnClickListener {
            viewModel.getTracks(binding.editTextSearch.text.toString())
        }

        binding.clearIcon.visibility =
            clearButtonVisibility(binding.editTextSearch.text)

        binding.editTextSearch.requestFocus()

        binding.clearIcon.setOnClickListener {
            clearSearch()
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun clearSearch() {
        trackListAdapter.tracks = arrayListOf()
        binding.editTextSearch.setText("")
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        viewModel.clearSearch()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_TEXT, binding.editTextSearch.toString())
    }


    private fun initHistory() {
        binding.trackList.adapter = trackListAdapter
        binding.clearHistory.setOnClickListener {
            viewModel.clearHistory()
        }
        showContent(Content.TRACKS_HISTORY)
    }

    private fun clickOnTrack(track: Track) {
        if (!viewModel.isClickable) return
        viewModel.addToHistory(track)
        viewModel.onTrackClick()
        findNavController().navigate(R.id.action_searchFragment_to_audioPlayerFragment)
    }


    private fun showContent(content: Content) {
        when (content) {
            Content.NOT_FOUND -> {
                binding.trackList.visibility = View.GONE
                binding.errLayout.visibility = View.VISIBLE
                binding.errMessage.visibility = View.VISIBLE
                binding.errMessage.text = resources.getText(R.string.nothing_found)
                binding.errPicture.setImageDrawable(
                    AppCompatResources.getDrawable(
                        binding.root.context,
                        R.drawable.icon_nothing_found
                    )
                )
                binding.errUpdateButton.visibility=View.GONE
                binding.trackList.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                trackListAdapter.tracks= ArrayList<Track>()
            }

            Content.ERROR -> {
                binding.trackList.visibility = View.GONE
                binding.errLayout.visibility = View.VISIBLE
                binding.errMessage.visibility = View.VISIBLE
                binding.errMessage.text = resources.getText(R.string.something_went_wrong)
                binding.errPicture.setImageDrawable(
                    AppCompatResources.getDrawable(
                        binding.root.context,
                        R.drawable.icon_network_problem
                    )
                )
                binding.errUpdateButton.visibility=View.VISIBLE
                binding.trackList.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }

            Content.TRACKS_HISTORY ->{
                if(trackListAdapter.tracks.size==0){
                    binding.clearHistory.visibility=View.GONE
                    binding.historyHeader.visibility=View.GONE
                }else{
                    binding.clearHistory.visibility=View.VISIBLE
                    binding.historyHeader.visibility=View.VISIBLE
                }
                trackListAdapter.notifyDataSetChanged()
                binding.trackLayout.visibility = View.VISIBLE
                binding.trackList.visibility = View.VISIBLE
                binding.errLayout.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }
            Content.SEARCH_RESULT -> {
                binding.clearHistory.visibility=View.GONE
                binding.historyHeader.visibility=View.GONE
                trackListAdapter.notifyDataSetChanged()
                binding.trackLayout.visibility = View.VISIBLE
                binding.trackList.visibility = View.VISIBLE
                binding.errLayout.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }


            Content.LOADING -> {
                binding.clearHistory.visibility = View.GONE
                binding.historyHeader.visibility = View.GONE
                binding.trackList.visibility = View.GONE
                binding.errLayout.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        private const val INPUT_TEXT = "INPUT_TEXT"
    }
}





