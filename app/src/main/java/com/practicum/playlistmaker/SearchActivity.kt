package com.practicum.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    private var currentValue: String = TEXT
    private val iTunesbasedUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesbasedUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private lateinit var errMessage: TextView
    private lateinit var errPicture: ImageView
    private lateinit var errLayout: LinearLayout
    private lateinit var errUpdateButton: MaterialButton
    private val iTunesService = retrofit.create(ITunesAPI::class.java)
    private val trackList = ArrayList<Track>()
    private val adapter = TrackListAdapter(trackList)
    private lateinit var inputEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        errMessage = findViewById(R.id.errMessage)
        errPicture = findViewById(R.id.errPicture)
        errLayout = findViewById(R.id.errLayout)
        errUpdateButton = findViewById(R.id.errUpdateButton)
        inputEditText = findViewById<EditText>(R.id.editTextSearch)

        val buttonBack = findViewById<ImageView>(R.id.buttonBack)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val recyclerView = findViewById<RecyclerView>(R.id.trackList)

        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = adapter

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendRequest()
            }
            false
        }

        buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        errUpdateButton.setOnClickListener{
            sendRequest()
        }

        clearButton.setOnClickListener {
            inputEditText.text.clear()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            clearPlaylist()
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
                if (s.isNullOrEmpty()) {

                } else {
                    currentValue = s.toString()
                }
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
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

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }


    private fun sendRequest() {
        if (inputEditText.text.isNotEmpty()) {
            errLayout.visibility = View.GONE
            iTunesService.findTrack(inputEditText.text.toString())
                .enqueue(object : Callback<TracksResponse> {
                    override fun onResponse(
                        call: Call<TracksResponse>,
                        response: Response<TracksResponse>
                    ) {
                        if (response.code() == 200) {
                            trackList.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackList.addAll(response.body()?.results!!)
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
                                R.drawable.icon_network_problem,true
                            )
                        }
                    }

                    override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                        showMessage(
                            getString(R.string.something_went_wrong),
                            R.drawable.icon_network_problem,true
                        )
                    }
                })
            true
        }
    }


    private fun showMessage(text: String, icon: Int, showButton: Boolean=false) {
        clearPlaylist()
        errLayout.visibility = View.VISIBLE
        errPicture.setImageResource(icon)
        trackList.clear()
        adapter.notifyDataSetChanged()
        errMessage.text = text
        if (showButton) {
            errUpdateButton.visibility = View.VISIBLE
        } else {
            errUpdateButton.visibility = View.GONE
        }
    }

    private fun clearPlaylist(){
        trackList.clear()
        adapter.notifyDataSetChanged()
    }
    companion object {
        const val SAVED_TEXT = "SAVED_TEXT"
        const val TEXT = ""
    }
}




