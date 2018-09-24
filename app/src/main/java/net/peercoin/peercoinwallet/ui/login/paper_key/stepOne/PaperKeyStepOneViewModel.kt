package net.peercoin.peercoinwallet.ui.login.paper_key.stepOne

import androidx.lifecycle.ViewModel;
import java.util.*
import kotlin.collections.ArrayList

class PaperKeyStepOneViewModel(private val neededWordsSize: Int) : ViewModel() {

    fun getRandomWords(): List<String> {
        val words: MutableList<String> = ArrayList()
        val positions: MutableList<Int> = ArrayList()
        var counter: Int = 0

        while (counter < neededWordsSize) {
            val randomNumber: Int = (1..list.size-1).random()
            if (!positions.contains(randomNumber)) {
                words.add(list[randomNumber])
                positions.add(randomNumber)
                counter++
            }
        }
        return Arrays.asList("test", "dva")
    }

    val list: List<String> = Arrays.asList("acid", "case", "enroll", "fox",
            "green", "junk", "magnet", "media", "price", "safe", "unknown", "water")

    fun IntRange.random() = (Math.random() * ((endInclusive + 1) - start) + start).toInt()

}

