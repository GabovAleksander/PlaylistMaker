package com.practicum.playlistmaker.main.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.MediaActivity
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonSearch = findViewById<MaterialButton>(R.id.buttonSearch)
        val buttonMedia = findViewById<MaterialButton>(R.id.buttonMedia)
        val buttonSettings = findViewById<MaterialButton>(R.id.buttonSettings)

        buttonSearch.setOnClickListener {
            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }

        buttonMedia.setOnClickListener {
            val displayIntent = Intent(this, MediaActivity::class.java)
            startActivity(displayIntent)
        }

        buttonSettings.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }
}