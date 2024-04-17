package com.practicum.playlistmaker.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.TrackGetterImpl
import com.practicum.playlistmaker.domain.PlayerInteractor
import com.practicum.playlistmaker.presentation.PlayerInteractorImpl
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private val delay = 300L
    private var liked = false

    private var mediaPlayer: PlayerInteractor = PlayerInteractorImpl()
    private lateinit var playButton: Button
    private lateinit var playTime: TextView
    private var mainThreadHandler: Handler? = null
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        val trackGetter= TrackGetterImpl()
        val image = findViewById<ImageView>(R.id.imageTrack_Track)
        val name = findViewById<TextView>(R.id.trackName)
        val artist = findViewById<TextView>(R.id.artistName)
        val duration = findViewById<TextView>(R.id.duration)
        val album = findViewById<TextView>(R.id.album)
        val year = findViewById<TextView>(R.id.year)
        val genre = findViewById<TextView>(R.id.genre)
        val country = findViewById<TextView>(R.id.country)
        playTime = findViewById<TextView>(R.id.playTime)
        playButton = findViewById<Button>(R.id.button_play)
        val likeButton = findViewById<Button>(R.id.button_like)
        val checkButton = findViewById<Button>(R.id.button_check)
        val track=trackGetter.getTrack(TRACK_KEY, intent)

        Glide.with(this).load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.icon_no_picture_big).into(image)
        name.text = track.trackName
        artist.text = track.artistName
        duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        album.text = track.collectionName
        year.text = track.releaseDate.substring(0, 4)
        genre.text = track.primaryGenreName
        country.text = track.country
        playTime.text = dateFormat.format(0)
        mediaPlayer.url = track.previewUrl

        val buttonLeft = findViewById<ImageView>(R.id.buttonBack)
        buttonLeft.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        likeButton.setOnClickListener {
            if (liked) {
                likeButton.background = getDrawable(R.drawable.button_heart_inactive)
                liked = false
            } else {
                likeButton.background = getDrawable(R.drawable.button_heart)
                liked = true
            }
        }

        checkButton.setOnClickListener {
            Toast.makeText(this@PlayerActivity, R.string.trackAdded, Toast.LENGTH_SHORT).show()
        }
        mainThreadHandler = Handler(Looper.getMainLooper())
        mediaPlayer.preparePlayer ( callback = {playButton.background=getDrawable(R.drawable.button_play)})

        playButton.setOnClickListener {
            mediaPlayer.playbackControl(
                callbackWhenPlay = {playButton.background=getDrawable(R.drawable.button_play)},
                callbackWhenPause = {playButton.background=getDrawable(R.drawable.button_pause)
                startTimer()}
            )
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pausePlayer { playButton.background=getDrawable(R.drawable.button_play) }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }



    private fun startTimer() {
        mainThreadHandler?.post(updateTimer())
    }

    private fun updateTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                if (mediaPlayer.playerState == PlayerInteractor.Status.STATE_PLAYING) {
                    playTime.text = dateFormat.format(mediaPlayer.getCurrentPosition())
                    mainThreadHandler?.postDelayed(this, delay)
                }
            }
        }
    }


}