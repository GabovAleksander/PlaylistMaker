package com.practicum.playlistmaker.media.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.Constants
import com.practicum.playlistmaker.media.ui.fragments.Favorites
import com.practicum.playlistmaker.media.ui.fragments.Playlists

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            Constants.INDEX_FIRST -> Favorites.newInstance()
            else -> Playlists.newInstance()
        }
    }

}