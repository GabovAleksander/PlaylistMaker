package com.practicum.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel=ViewModelProvider(this,SettingsViewModel.getViewModelFactory(application as App))[SettingsViewModel::class.java]

        initButtonBack()
        initSwitch()
        initButtonShare()
        initButtonSupport()
        initButtonAgreement()

    }

    private fun initButtonBack(){
        binding.buttonBack.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                finish()
            }
        }
    }
    private fun initSwitch(){
        binding.themeSwitcher.apply {
            isChecked=viewModel.isDarkThemeOn()
            setOnCheckedChangeListener{_, isChecked->viewModel.switchTheme(isChecked)}
        }
    }

    private fun initButtonShare() {
        binding.buttonShare.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                putExtra(
                    Intent.EXTRA_TEXT, getResources().getString(R.string.urlToCourse)
                )
                type = "text/plain"
                startActivity(Intent.createChooser(this, null))
            }
        }
    }

    private fun initButtonSupport(){
        binding.buttonSupport.setOnClickListener{
            Intent(Intent.ACTION_SEND).apply {
                val shareIntent = Intent(Intent.ACTION_SENDTO)
                shareIntent.data = Uri.parse("mailto:")
                shareIntent.putExtra(
                    Intent.EXTRA_EMAIL, arrayOf(getResources().getString(R.string.email))
                )
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.topic))
                shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.messageBody))
                startActivity(shareIntent)
            }
        }
    }

    private fun initButtonAgreement(){
        binding.buttonAgreement.setOnClickListener{
            Intent(Intent.ACTION_SEND).apply {
                 val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.offer)))
                startActivity(browserIntent);
            }
        }
    }

}
