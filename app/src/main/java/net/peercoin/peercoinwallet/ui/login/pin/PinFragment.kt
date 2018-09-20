package net.peercoin.peercoinwallet.ui.login.pin

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_pin.*
import net.peercoin.peercoinwallet.R
import net.peercoin.peercoinwallet.ui.login.LoginActivity


class PinFragment : Fragment() {
    companion object {
        fun newInstance() = PinFragment()
        @JvmStatic
        val PIN_SIZE = 6
        @JvmStatic
        val ENTER_PIN = 32
        @JvmStatic
        val CONFIRM_PIN = 64
    }

    private lateinit var password: String
    private lateinit var pinAdapter: PinAdapter
    private var type: Int = ENTER_PIN

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pin, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupTextSwitcher()
        setupRecyclerView()
        setupEditText()
    }

    fun setupRecyclerView() {
        pinAdapter = PinAdapter(PIN_SIZE)
        rvPinIndicator.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvPinIndicator.adapter = pinAdapter
    }

    fun setupTextSwitcher() {
        tsPin.setFactory {
            val textView = TextView(activity)
            textView.gravity = Gravity.CENTER
            textView.textSize = 17f
            textView.setTextColor(Color.BLACK)
            textView
        }
        val animIn = AnimationUtils.loadAnimation(activity, android.R.anim.slide_in_left)
        tsPin.inAnimation = animIn
        val out = AnimationUtils.loadAnimation(activity, android.R.anim.slide_out_right)
        tsPin.outAnimation = out
        val text = activity?.resources?.getText(R.string.pin_message_1)
        tsPin.setCurrentText(text)
    }

    private fun setupEditText() {

        etPin.requestFocus()
        etPin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val pass = etPin.text.toString()
                pinAdapter.onTextChanged(pass.length)

                if (pass.length == PIN_SIZE) {
                    //Enter pin means initialization of pin
                    if (type == ENTER_PIN) {
                        type = CONFIRM_PIN
                        password = pass
                        val text = activity?.resources?.getText(R.string.pin_message_3)
                        tsPin.setText(text)
                        resetAdapter()

                    } else {
                        if (password == pass) {
                            (activity as LoginActivity).registerSuccessful()
                            toggleLayoutChanges()
                        } else {
                            type = ENTER_PIN
                            val text = activity?.resources?.getText(R.string.pin_message_1)
                            tsPin.setCurrentText(text)
                            resetAdapter()
                        }
                    }
                }
            }
        })
    }

    fun toggleLayoutChanges() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
        tsPin.visibility = View.INVISIBLE
        rvPinIndicator.visibility = View.INVISIBLE
        llCautionMessage.visibility = View.INVISIBLE

        ivDone.y = 800f
        ivDone.visibility = View.VISIBLE
        ivDone.animate().translationY(-40f).duration = 650
        Handler().postDelayed({
            tvSuccessMessage.animate().alpha(1f).duration = 400
        }, 600L)
    }

    fun resetAdapter() {
        Handler().postDelayed({
            pinAdapter.reset()
            etPin.text = null
        }, 100L)
    }


}
