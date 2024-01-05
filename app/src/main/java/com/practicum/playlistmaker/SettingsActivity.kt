package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBack=findViewById<TextView>(R.id.buttonBack)
        val buttonShare=findViewById<FrameLayout>(R.id.buttonShare)
        val buttonAgreement=findViewById<FrameLayout>(R.id.buttonAgreement)
        val buttonSupport=findViewById<FrameLayout>(R.id.buttonSupport)


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