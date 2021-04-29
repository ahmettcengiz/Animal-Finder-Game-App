package com.example.functions
import android.app.Activity
import android.content.Context
import com.example.objects.Animal
import java.util.ArrayList

class LevelDesign {
    fun decideLevel(level: Int):ArrayList<Animal>{
        val AnimalList = DatasetFunction().getAnimals().shuffled()
        val newAnimalList = ArrayList<Animal>()
        for (k in 0..level-1){
            newAnimalList.add(AnimalList.get(k))
        }
        return newAnimalList
    }
    fun decideCorrectAnimal(newAnimalList:ArrayList<Animal>):Animal{
        val random = (0..newAnimalList.size-1).random()
        val correctAnimal = newAnimalList.get(random)

        return correctAnimal
    }
    fun highScore(score:Int,c:Context){
        val sharedPref = c.getSharedPreferences("settings_pref", Activity.MODE_PRIVATE)
        val highScore = sharedPref!!.getInt("score", 0)
        if (score>highScore){
            val editor =sharedPref!!.edit()
            editor.putInt("score",score)
            editor.apply()
        }


    }
}