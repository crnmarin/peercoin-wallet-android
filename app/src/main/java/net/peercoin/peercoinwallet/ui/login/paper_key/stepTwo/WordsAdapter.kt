package net.peercoin.peercoinwallet.ui.login.paper_key.stepTwo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_word.view.*
import net.peercoin.peercoinwallet.R

class WordsAdapter(private val wordsMap: HashMap<Int, String>,
                   private val keys: IntArray = wordsMap.keys.toIntArray(),
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
        holder.itemView.etWord.hint = wordString.toLowerCase() + " #" + wordIndex.toString()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
