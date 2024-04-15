package com.practicum.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private var liked = false
    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private lateinit var playButton: Button
    private lateinit var playTime: TextView
    private var mainThreadHandler: Handler? = null
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
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
        playTime = findViewById<TextView>(R.id.playTime)
        playButton = findViewById<Button>(R.id.button_play)
        val likeButton = findViewById<Button>(R.id.button_like)
        val checkButton = findViewById<Button>(R.id.button_check)

        val gson = Gson()
        val json = intent.getStringExtra(TRACK_KEY)
        val track = gson.fromJson(json, Track::class.java)

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
        var url: String? = track.previewUrl

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
        preparePlayer(url)
        playButton.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun preparePlayer(url: String?) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.background = getDrawable(R.drawable.button_play)
            playerState = STATE_PREPARED
            playTime.text = dateFormat.format(0)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.background = getDrawable(R.drawable.button_pause)
        playerState = STATE_PLAYING
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.background = getDrawable(R.drawable.button_play)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startTimer() {
        mainThreadHandler?.post(updateTimer())
    }

    private fun updateTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    playTime.text = dateFormat.format(mediaPlayer.currentPosition)
                    mainThreadHandler?.postDelayed(this, UPDATE_DELAY)
                }
            }
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val UPDATE_DELAY = 300L
    }
}