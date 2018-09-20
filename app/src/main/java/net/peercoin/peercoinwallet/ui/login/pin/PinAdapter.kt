package net.peercoin.peercoinwallet.ui.login.pin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_pin.view.*
import net.peercoin.peercoinwallet.R


class PinAdapter(private val size: Int) : RecyclerView.Adapter<PinAdapter.ViewHolder>() {

    private var count: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pin, parent, false))
    }

    override fun getItemCount(): Int {
        return size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < count)
            holder.itemView.ivPinItem.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_pin_green))
        else
            holder.itemView.ivPinItem.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_pin_black))
    }

    fun onTextChanged(count: Int) {
            this.count = count
            notifyDataSetChanged()
    }

    fun reset() {
        this.count = 0
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}