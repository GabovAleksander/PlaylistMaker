package com.practicum.playlistmaker.media.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.main.ui.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MediaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaBinding
    private val mediaViewModel: MediaViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initButtonBack()

    }


    private fun initButtonBack() {
        binding.buttonBack.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                finish()
            }
        }
    }
}