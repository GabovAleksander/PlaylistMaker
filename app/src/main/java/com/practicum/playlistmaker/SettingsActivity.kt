package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBack = findViewById<ImageView>(R.id.buttonBack)
        val themeSwitcher  = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val switchLayout = findViewById<FrameLayout>(R.id.switchLayout)
        val buttonShare = findViewById<FrameLayout>(R.id.buttonShare)
        val buttonAgreement = findViewById<FrameLayout>(R.id.buttonAgreement)
        val buttonSupport = findViewById<FrameLayout>(R.id.buttonSupport)
        val themeSharedPreferences = getSharedPreferences(THEME, MODE_PRIVATE)


        themeSwitcher.isChecked= (this.application as App).darkTheme;

        buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }



       // switchLayout.setOnClickListener {
       //     themeSwitcher.isChecked = !themeSwitcher.isChecked
       //     changeTheme()
        //}

        //Поменять тему
        themeSwitcher.setOnCheckedChangeListener  { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            themeSharedPreferences.edit()
                .putBoolean(DARK,checked)
                .apply()
            (this.application as App).darkTheme=checked
            //changeTheme()
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

    companion object{
        private const val THEME = "theme"
        private const val DARK = "dark"
    }

}
