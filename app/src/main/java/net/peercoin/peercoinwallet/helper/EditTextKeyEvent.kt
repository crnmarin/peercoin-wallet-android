package net.peercoin.peercoinwallet.helper

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.EditText
import net.peercoin.peercoinwallet.helper.EditTextKeyEvent.KeyboardListener




class EditTextKeyEvent : EditText {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun dispatchKeyEventPreIme(event: KeyEvent?): Boolean {
        if (event?.action == KeyEvent.ACTION_DOWN) {
            if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                listener?.backPressed()
                return true
            }
        }
        return super.dispatchKeyEventPreIme(event)
    }

    fun setOnKeyboardListener(listener: KeyboardListener) {
        this.listener = listener
    }

    var listener: KeyboardListener? = null

    interface KeyboardListener {
        fun backPressed()
    }

}