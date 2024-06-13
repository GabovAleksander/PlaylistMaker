package com.practicum.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {

        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSwitch()
        initButtonShare()
        initButtonSupport()
        initButtonAgreement()

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
