package com.example.animalfindergame
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("settings_pref", Activity.MODE_PRIVATE)
        val score = sharedPref!!.getInt("score",0)
        scoreTextView.setText(""+score.toString())
        val ttb =AnimationUtils.loadAnimation(this,R.anim.ttb)
        val stb =AnimationUtils.loadAnimation(this,R.anim.stb)

        headertitle.startAnimation(ttb)
        subtitle.startAnimation(ttb)
        ic_cards.startAnimation(stb)
        textViewHighScore.startAnimation(stb)
        scoreTextView.startAnimation(stb)
        firstOption.startAnimation(stb)
        secondOption.startAnimation(stb)

        firstOption.setOnClickListener{
            val intent = Intent(this@MainActivity, NormalMode::class.java)
            startActivity(intent)
        }
        secondOption.setOnClickListener{
            val intent = Intent(this@MainActivity, TimeRushMode::class.java)
            startActivity(intent)
        }


    }


}