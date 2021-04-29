package com.example.functions
import com.example.animalfindergame.R
import com.example.objects.Animal

class DatasetFunction {
     fun getAnimals(): List<Animal> {
        val animals = listOf(
            Animal("bee", R.drawable.ic_bee,R.raw.bee),
            Animal("cat",R.drawable.ic_cat,R.raw.cat),
            Animal("cow",R.drawable.ic_cow,R.raw.cow),
            Animal("dog",R.drawable.ic_dog,R.raw.dog),
            Animal("duck",R.drawable.ic_duck,R.raw.duck),
            Animal("frog",R.drawable.ic_frog,R.raw.frog),
            Animal("lion",R.drawable.ic_lion,R.raw.lion),
            Animal("monkey",R.drawable.ic_monkey,R.raw.monkey),
            Animal("rooster",R.drawable.ic_rooster,R.raw.rooster),
            Animal("wolf",R.drawable.ic_wolf,R.raw.wolf),
            Animal("horse", R.drawable.ic_horse,R.raw.horse),
            Animal("elephant",R.drawable.ic_elephant,R.raw.elephant),
            Animal("turkey",R.drawable.ic_turkey,R.raw.turkey),
            Animal("sheep",R.drawable.ic_sheep,R.raw.sheep),
            Animal("snake",R.drawable.ic_snake,R.raw.rattlesnake),
            Animal("crow",R.drawable.ic_raven,R.raw.crow),
            Animal("bird",R.drawable.ic_robin,R.raw.robin),
            Animal("owl",R.drawable.ic_owl,R.raw.owl),
        )
        return animals
    }

}