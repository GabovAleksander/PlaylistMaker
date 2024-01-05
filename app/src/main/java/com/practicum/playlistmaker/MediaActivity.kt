package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MediaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val buttonBack=findViewById<TextView>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()}
    }
}