package com.example.animalfindergame
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adapters.AnimalListAdapter
import com.example.functions.LevelDesign
import com.example.objects.Animal
import kotlinx.android.synthetic.main.activity_level1.*
import kotlinx.android.synthetic.main.win_dialog_layout.*
import java.util.*
import kotlin.collections.ArrayList

class LevelSystem : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
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
        setContentView(R.layout.activity_level1)
        levelText.setText("LEVEL"+level.toString())
        tts = TextToSpeech(this, this)
        dialog = Dialog(this)
        scoreText.setText(""+score.toString())

        val newAnimalList = LevelDesign().decideLevel(levelMap.get(level)!!)
        var correctAnimal = LevelDesign().decideCorrectAnimal(newAnimalList)

        animalImageView!!.setOnClickListener {
            //speakOut()
            mediaPlayer = MediaPlayer.create(this, correctAnimal.soundUrl)
            mediaPlayer!!.start()
        }


        countObject = createTimer(levelTime.get(level)!!)
        countObject.start()

        adapter = AnimalListAdapter(newAnimalList, object : AnimalListAdapter.OnClickListener {
            override fun onItemClick(objects: Animal) {
                countObject.cancel()

                if(level==5 && objects.name == correctAnimal.name && temp==2 ){
                    temp=0
                    score+=10
                    mediaPlayer!!.stop()
                    val intent = Intent(this@LevelSystem, MainActivity::class.java)
                    LevelDesign().highScore(score,applicationContext)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else if (objects.name == correctAnimal.name && temp==2){
                    mediaPlayer!!.stop()
                    level++
                    countObject = createTimer(levelTime.get(level)!!)
                    openwinDialog(countObject)
                    temp=0
                    score += 10
                    levelMap.get(level)?.let { replaceOldListWithNewList(newAnimalList, it) }
                    correctAnimal=LevelDesign().decideCorrectAnimal(newAnimalList)
                }
                else if (objects.name == correctAnimal.name) {
                    mediaPlayer!!.stop()
                    score += 10
                    temp++
                    levelMap.get(level)?.let { replaceOldListWithNewList(newAnimalList, it) }
                    correctAnimal=LevelDesign().decideCorrectAnimal(newAnimalList)
                    countObject.start()
                }
                else {
                    temp=0
                    mediaPlayer!!.stop()
                    val intent = Intent(this@LevelSystem, MainActivity::class.java)
                    LevelDesign().highScore(score,applicationContext)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                levelText.setText("LEVEL"+level.toString())
                scoreText.setText(""+score.toString())
            }

        })

        recyclerview.adapter=adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

    }

    fun createTimer(countTempp:Int):CountDownTimer{
        var countObject = object : CountDownTimer((countTempp.times(10000)).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                TimeText.text = (millisUntilFinished/1000).toString()
                counter++
            }
            override fun onFinish() {
                TimeText.text = "Finished"
                val intent = Intent(this@LevelSystem, MainActivity::class.java)
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

    private fun speakOut() {
        val text = "Hello Ahmet"
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
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
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

    fun openwinDialog(k:CountDownTimer){
        k.cancel()
        dialog!!.setContentView(R.layout.win_dialog_layout)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCanceledOnTouchOutside(false);
        val continueButton: Button = dialog!!.findViewById(R.id.continueButton)

        dialog!!.show()
       continueButton.setOnClickListener(){
           dialog!!.dismiss()
           k.start()
       }
    }

}