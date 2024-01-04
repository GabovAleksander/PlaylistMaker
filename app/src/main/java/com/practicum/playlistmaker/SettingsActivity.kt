package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBack=findViewById<FrameLayout>(R.id.back)
        val buttonShare=findViewById<FrameLayout>(R.id.Share)
        val buttonAgreement=findViewById<FrameLayout>(R.id.Agreement)
        val buttonSupport=findViewById<FrameLayout>(R.id.Support)


        buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        //Анонимный класс
        val buttonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@SettingsActivity, "Делимся приложением!", Toast.LENGTH_SHORT).show()
            }
        }
        buttonShare.setOnClickListener(buttonClickListener)

        //Лямбда
        buttonAgreement.setOnClickListener { Toast.makeText(this@SettingsActivity,"Читаем пользовательское соглашение!", Toast.LENGTH_SHORT).show() }
        //Лямбда2
        buttonSupport.setOnClickListener { Toast.makeText(this@SettingsActivity,"Обращаемся в поддержку!", Toast.LENGTH_SHORT).show() }




    }
}