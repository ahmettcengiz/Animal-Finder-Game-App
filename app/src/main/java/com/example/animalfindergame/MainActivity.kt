package com.example.animalfindergame
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_level1.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("settings_pref", Activity.MODE_PRIVATE)
        val score = sharedPref!!.getInt("score",0)
        textView2.setText(score.toString())

        buttona.setOnClickListener {
            val intent = Intent(this@MainActivity, LevelSystem::class.java)
            startActivity(intent)

        }

    }

}