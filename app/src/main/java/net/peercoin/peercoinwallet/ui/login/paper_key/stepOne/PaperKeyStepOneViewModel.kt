package net.peercoin.peercoinwallet.ui.login.paper_key.stepOne

import androidx.lifecycle.ViewModel
import java.util.*

class PaperKeyStepOneViewModel(private val neededWordsSize: Int) : ViewModel() {

    fun getRandomWords(): HashMap<Int, String> {
        val wordsMap: HashMap<Int, String> = hashMapOf()
        var counter = 0

        while (counter <= neededWordsSize) {
            val randomNumber: Int = (1..list.size - 1).random()
            if (!wordsMap.contains(randomNumber)) {
                wordsMap[randomNumber] = list[randomNumber]
                counter++
            }
        }
        return wordsMap
    }

    val list: List<String> = Arrays.asList("acid", "case", "enroll", "fox",
            "green", "junk", "magnet", "media", "price", "safe", "unknown", "water")

    fun IntRange.random() = (Math.random() * ((endInclusive + 1) - start) + start).toInt()

}

