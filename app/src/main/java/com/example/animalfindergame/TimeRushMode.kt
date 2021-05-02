package com.example.animalfindergame
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adapters.AnimalListAdapter
import com.example.functions.LevelDesign
import com.example.objects.Animal
import kotlinx.android.synthetic.main.activity_level.*
import kotlinx.android.synthetic.main.win_dialog_layout.*
import java.util.*
import kotlin.collections.ArrayList

class TimeRushMode : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var correctTts: TextToSpeech? = null
    var mediaPlayer: MediaPlayer? = null
    var adapter: AnimalListAdapter? = null
    var dialog:Dialog?=null
    var temp=0
    var level=1
    var levelMap= hashMapOf<Int,Int>(1 to 2, 2 to 3, 3 to 4, 4 to 6, 5 to 8)
    var levelTime= hashMapOf<Int,Int>(1 to 5, 2 to 4, 3 to 3, 4 to 2, 5 to 1)
    var score = 0
    var counter = 0
    lateinit var countObject:CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)
        levelText.setText("LEVEL "+level.toString())
        tts = TextToSpeech(this, this)
        correctTts = TextToSpeech(this, this)
        dialog = Dialog(this)
        scoreText.setText("Score : "+score.toString())

        val newAnimalList = LevelDesign().decideLevel(levelMap.get(level)!!)
        var correctAnimal = LevelDesign().decideCorrectAnimal(newAnimalList)

        val handler = Handler()
        handler.postDelayed(Runnable {
            mediaPlayer = MediaPlayer.create(applicationContext, correctAnimal.soundUrl)
            speakOut(correctAnimal)
        }, 100)

        tts!!.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onDone(utteranceId: String?) {
                if (utteranceId=="1") {
                    mediaPlayer!!.start()
                }

            }
            override fun onError(utteranceId: String?) {

            }
            override fun onStart(utteranceId: String?) {

            }
        })

        animalImageView!!.setOnClickListener {
            mediaPlayer = MediaPlayer.create(applicationContext, correctAnimal.soundUrl)
            speakOut(correctAnimal)

        }

        countObject = createTimer(levelTime.get(level)!!)
        countObject.start()

        adapter = AnimalListAdapter(newAnimalList, object : AnimalListAdapter.OnClickListener {
            override fun onItemClick(objects: Animal) {
                countObject.cancel()

                if(level==5 && objects.name == correctAnimal.name && temp==2 ){
                    stopMediaPlayer()
                    temp=0
                    score+=10
                    speak("Congratulations You Won", 1600)
                    val intent = Intent(this@TimeRushMode, MainActivity::class.java)
                    LevelDesign().highScore(score,applicationContext)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else if (objects.name == correctAnimal.name && temp==2){
                    stopMediaPlayer()
                    speak("You got it", 1150)
                    level++
                    countObject = createTimer(levelTime.get(level)!!)
                    temp=0
                    score += 10
                    levelMap.get(level)?.let { replaceOldListWithNewList(newAnimalList, it) }
                    correctAnimal=LevelDesign().decideCorrectAnimal(newAnimalList)
                    openwinDialog(countObject,correctAnimal)
                }
                else if (objects.name == correctAnimal.name) {
                    stopMediaPlayer()
                    speak("You got it", 1150)
                    score += 10
                    temp++
                    levelMap.get(level)?.let { replaceOldListWithNewList(newAnimalList, it) }
                    correctAnimal=LevelDesign().decideCorrectAnimal(newAnimalList)
                    countObject.start()
                    mediaPlayer = MediaPlayer.create(applicationContext, correctAnimal.soundUrl)
                    speakOut(correctAnimal)
                }
                else {
                    temp=0
                    stopMediaPlayer()
                    speak("Sorry Wrong Answer", 1300)
                    tts!!.stop()
                    val intent = Intent(this@TimeRushMode, MainActivity::class.java)
                    LevelDesign().highScore(score,applicationContext)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                levelText.setText("LEVEL "+level.toString())
                scoreText.setText("Score : "+score.toString())
            }

        })

        recyclerview.adapter=adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

    }
    fun stopMediaPlayer(){
        if(mediaPlayer!=null){
            mediaPlayer!!.stop()
        }
    }

    fun createTimer(countTempp:Int):CountDownTimer{
        var countObject = object : CountDownTimer((countTempp.times(10000)).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                TimeText.text = (millisUntilFinished/1000).toString()
                counter++
            }
            override fun onFinish() {
                TimeText.text = "Finished"
                val intent = Intent(this@TimeRushMode, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
        return countObject
    }

     fun replaceOldListWithNewList(newAnimalList: ArrayList<Animal>,level:Int) {
        newAnimalList.clear()
        var newList: ArrayList<Animal>
        newList = LevelDesign().decideLevel(level)
        newAnimalList.addAll(newList)
        adapter?.notifyDataSetChanged()
    }

    private fun speakOut(correctAnimal:Animal) {
        val name = correctAnimal.name.toString()
        val text = "“Find the "+ name+","+name+" makes sound"
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "1")
    }
    fun speak(text: String, ms: Long) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "2")
        Thread.sleep(ms)
    }

    override fun onInit(p0: Int) {
        if (p0 == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            } else {
                animalImageView!!.isEnabled = true
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }
    }

    public override fun onDestroy() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

    fun openwinDialog(k:CountDownTimer,correctAnimal: Animal){
        k.cancel()
        TimeText.visibility= View.INVISIBLE
        dialog!!.setContentView(R.layout.win_dialog_layout)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCanceledOnTouchOutside(false);
        val continueButton: Button = dialog!!.findViewById(R.id.continueButton)

        dialog!!.show()
       continueButton.setOnClickListener(){
           dialog!!.dismiss()
           mediaPlayer = MediaPlayer.create(applicationContext, correctAnimal.soundUrl)
           speakOut(correctAnimal)
           TimeText.visibility= View.VISIBLE
           k.start()
       }
    }

}