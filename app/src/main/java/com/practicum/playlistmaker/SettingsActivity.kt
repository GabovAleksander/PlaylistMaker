package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBack = findViewById<ImageView>(R.id.buttonBack)
        val switchButton = findViewById<Switch>(R.id.switchButton)
        val switchLayout = findViewById<FrameLayout>(R.id.switchLayout)
        val buttonShare = findViewById<FrameLayout>(R.id.buttonShare)
        val buttonAgreement = findViewById<FrameLayout>(R.id.buttonAgreement)
        val buttonSupport = findViewById<FrameLayout>(R.id.buttonSupport)


        buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }



        switchLayout.setOnClickListener {
            switchButton.isChecked = !switchButton.isChecked
            changeTheme()
        }

        //Поменять тему
        switchButton.setOnClickListener {
            changeTheme()
        }
        //Поделиться приложением
        buttonShare.setOnClickListener {
            val messengerIntent = Intent(Intent.ACTION_SEND)
            messengerIntent.putExtra(
                Intent.EXTRA_TEXT, getResources().getString(R.string.urlToCourse)
            )
            messengerIntent.setType("text/plain")
            startActivity(messengerIntent)
        }
        //Пользовательское соглашение
        buttonAgreement.setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.offer)))
            startActivity(browserIntent);
        }

        //Поддержка
        buttonSupport.setOnClickListener {
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

    private fun changeTheme() {
        Toast.makeText(this@SettingsActivity, "Переключаем тему!", Toast.LENGTH_SHORT).show()
    }

}