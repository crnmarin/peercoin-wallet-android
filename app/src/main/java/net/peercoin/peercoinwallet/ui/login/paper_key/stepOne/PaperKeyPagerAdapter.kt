package net.peercoin.peercoinwallet.ui.login.paper_key.stepOne

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import net.peercoin.peercoinwallet.R

class PaperKeyPagerAdapter(private val words: List<String>,
                           private val context: Context) : PagerAdapter() {

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.item_pager_word, container, false)
        val tvWord = view.findViewById<TextView>(R.id.tvWord)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        tvWord.text = words[position]
        tvTitle.text = getPageTitle(position)

        container.addView(view)

        return view
    }


    override fun getCount(): Int {
        return words.size
    }

    override fun getPageTitle(position: Int): CharSequence? {

        return (position + 1).toString() + " of " + (count).toString()
    }

}