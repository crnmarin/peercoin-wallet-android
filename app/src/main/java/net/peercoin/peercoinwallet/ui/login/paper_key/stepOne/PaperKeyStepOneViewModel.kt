package net.peercoin.peercoinwallet.ui.login.paper_key.stepOne

import androidx.lifecycle.ViewModel
import java.util.*

class PaperKeyStepOneViewModel(private val neededWordsSize: Int) : ViewModel() {

    fun getRandomWords(): HashMap<Int, String> {
        val wordsMap: HashMap<Int, String> = hashMapOf()
        var counter = 0

        while (counter <= neededWordsSize) {
            val randomNumber: Int = (0 until list.size).random()
            val key = randomNumber + 1
            if (!wordsMap.contains(key)) {
                //Key is random number + 1 because we show words with indexing 1,2,3...,
                wordsMap[key] = list[randomNumber] //list is indexing from 0...list.size
                counter++
            }
        }
        return wordsMap
    }

    val list: List<String> = Arrays.asList("acid", "case", "enroll", "fox",
            "green", "junk", "magnet", "media", "price", "safe", "unknown", "water")

    fun IntRange.random() = (Math.random() * ((endInclusive + 1) - start) + start).toInt()

}

