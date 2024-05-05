package com.practicum.playlistmaker.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.sharedPreferences.SearchHistory
import com.practicum.playlistmaker.data.dto.TracksResponse
import com.practicum.playlistmaker.data.network.ITunesAPI
import com.practicum.playlistmaker.domain.models.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val TRACK_KEY = "track_key"

class SearchActivity : AppCompatActivity() {
    private var currentValue: String = TEXT
    private val iTunesbasedUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesbasedUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesAPI::class.java)
    private lateinit var errMessage: TextView
    private lateinit var historyHeader: TextView
    private lateinit var errPicture: ImageView
    private lateinit var errLayout: LinearLayout
    private lateinit var errUpdateButton: MaterialButton
    private lateinit var historyClearButton: MaterialButton
    private lateinit var searchHistory: SearchHistory
    private lateinit var progressBar: ProgressBar

    private val trackList = mutableListOf<Track>()
    private val historyTrackList = mutableListOf<Track>()
    private lateinit var adapter: TrackListAdapter
    private var isClickAllowed = true

    //private lateinit var historyAdapter:TrackListAdapter
    private lateinit var inputEditText: EditText
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        errMessage = findViewById(R.id.errMessage)
        errPicture = findViewById(R.id.errPicture)
        errLayout = findViewById(R.id.errLayout)
        errUpdateButton = findViewById(R.id.errUpdateButton)
        inputEditText = findViewById<EditText>(R.id.editTextSearch)
        historyHeader = findViewById<TextView>(R.id.historyHeader)
        historyClearButton = findViewById(R.id.clearHistory)
        progressBar = findViewById(R.id.progressBar)
        val buttonBack = findViewById<ImageView>(R.id.buttonBack)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val recyclerView = findViewById<RecyclerView>(R.id.trackList)
        val savedHistory = getSharedPreferences(HISTORY_KEY, MODE_PRIVATE)
        //val searchHistory: SearchHistory

        clearButton.isVisible = false
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        adapter = TrackListAdapter(trackList, {item: Track ->clickOnTrack(item)})
        recyclerView.adapter = adapter
        //historyAdapter = TrackListAdapter(historyTrackList,savedHistory)
        if (savedHistory.getString(HISTORY_KEY, "[]").equals("[]")) {
            historyHeader.isVisible=false
            historyClearButton.isVisible=false
            recyclerView.isVisible=false
            savedHistory.edit()
                .putString(HISTORY_KEY, Gson().toJson(listOf<Track>()))
                .apply()
            searchHistory = SearchHistory(savedHistory)

        } else {
            historyHeader.visibility = View.VISIBLE
            historyClearButton.visibility = View.VISIBLE
            searchHistory = SearchHistory(savedHistory)
            historyTrackList.clear()
            historyTrackList.addAll(searchHistory.loadHistory())
            //
            adapter.items.clear()
            adapter.items.addAll(historyTrackList)
            adapter.notifyDataSetChanged()
            //recyclerView.adapter = historyAdapter
            //historyAdapter.notifyDataSetChanged()
            recyclerView.isVisible=true
        }


        historyClearButton.setOnClickListener {
            searchHistory.clearHistory()
            historyTrackList.clear()
            adapter.items.clear()
            adapter.items.addAll(historyTrackList)
            adapter.notifyDataSetChanged()
            historyHeader.isVisible=false
            historyClearButton.isVisible=false
            recyclerView.isVisible=false
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendRequest()
            }
            false
        }

        buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        errUpdateButton.setOnClickListener {
            sendRequest()
        }

        clearButton.setOnClickListener {
            inputEditText.text.clear()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            clearPlaylist()
            adapter.items.clear()
            adapter.items.addAll(historyTrackList)
            errLayout.isVisible=false
        }

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            historyClearButton.isVisible =
                if (hasFocus && inputEditText.text.isEmpty() && historyTrackList.size > 0) true else false
            historyHeader.isVisible =
                if (hasFocus && inputEditText.text.isEmpty() && historyTrackList.size > 0) true else false
            //
            adapter.items.clear()
            adapter.items.addAll(historyTrackList)
            //recyclerView.adapter=historyAdapter
        }


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDebounce()
                if (s.isNullOrEmpty()) {
                    historyTrackList.clear()
                    historyTrackList.addAll(searchHistory.loadHistory())
                    if (historyTrackList.size > 0) {
                        //
                        adapter.items.clear()
                        adapter.items.addAll(historyTrackList)
                        adapter.notifyDataSetChanged()
                        //historyAdapter.notifyDataSetChanged()
                        //recyclerView.adapter = historyAdapter
                        recyclerView.isVisible=true
                        historyHeader.isVisible=true
                        historyClearButton.isVisible=true
                    }
                } else {
                    adapter.items.clear()
                    adapter.items.addAll(trackList)
                    currentValue = s.toString()
                    //recyclerView.adapter=adapter
                    recyclerView.isVisible = true
                    historyHeader.isVisible = false
                    historyClearButton.isVisible = false
                }
                clearButton.isVisible = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
        inputEditText.requestFocus()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_TEXT, currentValue)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle
    ) {
        super.onRestoreInstanceState(savedInstanceState)
        val inputEditText = findViewById<EditText>(R.id.editTextSearch)
        currentValue = savedInstanceState.getString(SAVED_TEXT, TEXT)
        inputEditText.setText(currentValue)
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !(s.isNullOrEmpty())
    }


    private fun sendRequest() {
        if (inputEditText.text.isNotEmpty()) {
            progressBar.isVisible=true
            errLayout.isVisible = false
            iTunesService.findTrack(inputEditText.text.toString())
                .enqueue(object : Callback<TracksResponse> {
                    override fun onResponse(
                        call: Call<TracksResponse>,
                        response: Response<TracksResponse>
                    ) {
                        progressBar.isVisible=false
                        if (response.code() == 200) {
                            trackList.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackList.addAll(response.body()?.results!!.toMutableList().map { a->Track(a.trackId, a.trackName, a.artistName, a.trackTimeMillis, a.artworkUrl100,a.previewUrl,a.collectionName,a.releaseDate,a.primaryGenreName,a.country) })
                                adapter.notifyDataSetChanged()
                            }
                            if (trackList.isEmpty()) {
                                showMessage(
                                    getString(R.string.nothing_found),
                                    R.drawable.icon_nothing_found
                                )
                            }
                        } else {
                            showMessage(
                                getString(R.string.something_went_wrong),
                                R.drawable.icon_network_problem, true
                            )
                        }
                    }

                    override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                        progressBar.isVisible=false
                        showMessage(
                            getString(R.string.something_went_wrong),
                            R.drawable.icon_network_problem, true
                        )
                    }
                })
            true
        }
    }


    private fun showMessage(text: String, icon: Int, showButton: Boolean = false) {
        clearPlaylist()
        errLayout.visibility = View.VISIBLE
        errPicture.setImageResource(icon)
        trackList.clear()
        adapter.notifyDataSetChanged()
        errMessage.text = text
        if (showButton) {
            errUpdateButton.isVisible=true
        } else {
            errUpdateButton.isVisible=false
        }
    }

    private fun clickOnTrack(item: Track){
        if(clickDebounce()) {
            searchHistory.saveTrack(item)
            val context = this
            val playerIntent = Intent(context, PlayerActivity::class.java)
            val gson = Gson()
            val json = gson.toJson(item)
            context.startActivity(playerIntent.putExtra(TRACK_KEY, json))
        }
    }


    private fun clearPlaylist() {
        trackList.clear()
        adapter.notifyDataSetChanged()
    }

    private val searchRunnable = Runnable {
        sendRequest()
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    override fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
        super.onDestroy()
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SAVED_TEXT = "SAVED_TEXT"
        private const val TEXT = ""
        private const val HISTORY_KEY = "history"
        private const val HISTORY_SIZE = 10
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}





