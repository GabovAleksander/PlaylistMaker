package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    var play=true
    var liked=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val image = findViewById<ImageView>(R.id.imageTrack_Track)
        val name = findViewById<TextView>(R.id.trackName)
        val artist = findViewById<TextView>(R.id.artistName)
        val duration = findViewById<TextView>(R.id.duration)
        val album = findViewById<TextView>(R.id.album)
        val year = findViewById<TextView>(R.id.year)
        val genre = findViewById<TextView>(R.id.genre)
        val country = findViewById<TextView>(R.id.country)
        val playTime = findViewById<TextView>(R.id.playTime)
        val playButton=findViewById<Button>(R.id.button_play)
        val likeButton=findViewById<Button>(R.id.button_like)
        val checkButton=findViewById<Button>(R.id.button_check)

        val gson = Gson()
        val json = intent.getStringExtra(TRACK_KEY)
        val track = gson.fromJson(json, Track::class.java)

        Glide.with(this).load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")).
        placeholder(R.drawable.icon_no_picture_big).into(image)
        name.text = track.trackName
        artist.text = track.artistName
        duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        album.text = track.collectionName
        year.text = track.releaseDate.substring(0,4)
        genre.text = track.primaryGenreName
        country.text = track.country
        playTime.text="00:00"


        val buttonLeft = findViewById<ImageView>(R.id.buttonBack)
        buttonLeft.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        playButton.setOnClickListener{
            if(play){
                playButton.background = getDrawable(R.drawable.button_pause)
                play=false
            }else{
                playButton.background = getDrawable(R.drawable.button_play)
                play=true
            }
        }

        likeButton.setOnClickListener{
            if(liked){
                likeButton.background = getDrawable(R.drawable.button_heart_inactive)
                liked=false
            }else{
                likeButton.background = getDrawable(R.drawable.button_heart)
                liked=true
            }
        }

        checkButton.setOnClickListener{
            Toast.makeText(this@PlayerActivity, R.string.trackAdded, Toast.LENGTH_SHORT).show()
        }

    }
}