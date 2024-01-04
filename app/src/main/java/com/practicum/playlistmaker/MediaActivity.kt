package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout

class MediaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val buttonBack=findViewById<FrameLayout>(R.id.back)
        buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()}
    }
}