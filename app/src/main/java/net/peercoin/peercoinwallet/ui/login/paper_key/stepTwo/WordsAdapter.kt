package net.peercoin.peercoinwallet.ui.login.paper_key.stepTwo

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_word.view.*
import net.peercoin.peercoinwallet.R

class WordsAdapter(private val wordsMap: HashMap<Int, String>,
                   private val keys: IntArray = wordsMap.keys.toIntArray(),
                   private val enteredWords: HashMap<Int, String> = HashMap<Int, String>(),
                   private val words: Array<String> = wordsMap.values.toTypedArray()) : RecyclerView.Adapter<WordsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false))
    }

    override fun getItemCount(): Int {
        return wordsMap.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wordIndex = keys.get(position)
        val wordString = holder.itemView.context.getString(R.string.word)
        holder.itemView.tilWord.hint = wordString.toLowerCase() + " #" + wordIndex.toString()
        holder.itemView.etWord.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                enteredWords[position] = "" + s
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

    }

    fun isCorrectFulfilled(): Boolean {
        var count = 0
        for (pos: Int in 0 until words.size) {

            if (words[pos] == enteredWords[pos]) {
                count++
            }
        }
        return count == words.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
