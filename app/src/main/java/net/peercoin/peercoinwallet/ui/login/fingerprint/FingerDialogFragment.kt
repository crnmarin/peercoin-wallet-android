package net.peercoin.peercoinwallet.ui.login.fingerprint

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_finger.*
import net.peercoin.peercoinwallet.R
import net.peercoin.peercoinwallet.helper.EditTextKeyEvent
import net.peercoin.peercoinwallet.ui.login.LoginActivity
import net.peercoin.peercoinwallet.ui.login.pin.PinAdapter
import net.peercoin.peercoinwallet.ui.login.pin.PinFragment


class FingerDialogFragment : DialogFragment() {

    companion object {
        val TAG: String = FingerDialogFragment::class.java.simpleName
        fun newInstance() = FingerDialogFragment()
    }

    private lateinit var fingerPinAdapter: FingerPinAdapter
    private lateinit var viewModel: FingerDialogViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_finger, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FingerDialogViewModel::class.java)

        //Show the view (by setting alpha) - than zoom out by setting default scaling.
        showIntroMessage()
        replaceIntroWithPin()
        setupRecyclerView()
    }

    fun setupRecyclerView() {
        ivClose.setOnClickListener { activity!!.finish() }
        fingerPinAdapter = FingerPinAdapter(PinFragment.PIN_SIZE)
        rvPin.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvPin.adapter = fingerPinAdapter
    }

    fun replaceIntroWithPin() {
        Handler().postDelayed({
            //replace the view with pin and other authentication
            llIntroMessage.animate().alpha(0f).duration = 100
        }, 2600)

        Handler().postDelayed({
            val imm = etPinConfirm.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etPinConfirm, SHOW_IMPLICIT)
            dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            flFingerContent.visibility = View.VISIBLE
            flFingerContent.animate().alpha(1f).duration = 500
            setupEditText()
        }, 2700)

    }

    fun showIntroMessage() {
        Handler().postDelayed({
            llIntroMessage.animate().alpha(1f).duration = 125
            llIntroMessage.animate().scaleX(1f).scaleY(1f).duration = 250
        }, 600)
    }

    private fun setupEditText() {

        etPinConfirm.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val pass = etPinConfirm.text.toString()
                fingerPinAdapter.onTextChanged(pass.length)
                if (pass.length == 6) {
                    dismiss()
                    (activity as LoginActivity).stepOnePaperKey()
                }
            }
        })

        etPinConfirm.isFocusableInTouchMode = true

        val listener = object : EditTextKeyEvent.KeyboardListener {
            override fun backPressed() {
                //Check if keyboard should be collapsible
                (activity as LoginActivity).onBackPressed()
            }
        }
        etPinConfirm.setOnKeyboardListener(listener)
    }

}
