package com.practicum.playlistmaker.media.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.Constants
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.main.ui.MainViewModel
import com.practicum.playlistmaker.main.ui.NavigationRouter
import org.koin.androidx.viewmodel.ext.android.viewModel


class MediaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaBinding
    private lateinit var tabMediator:TabLayoutMediator
    private var navigationRouter = MediaRouter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) {
                tab, position ->
            when(position) {
                Constants.INDEX_FIRST -> tab.text = resources.getString(R.string.favorites)
                else -> tab.text = resources.getString(R.string.playlists)
            }
        }

        tabMediator.attach()


        initButtonBack()

    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

    private fun initButtonBack() {
        binding.buttonBack.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                finish()
            }
        }
    }
}