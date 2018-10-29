package net.peercoin.peercoinwallet.ui.login.fingerprint

import android.app.KeyguardManager
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_finger.*
import net.peercoin.peercoinwallet.R
import net.peercoin.peercoinwallet.helper.EditTextKeyEvent
import net.peercoin.peercoinwallet.service.fingerprint.FingerAuthentication
import net.peercoin.peercoinwallet.service.fingerprint.FingerAuthenticationHandler
import net.peercoin.peercoinwallet.ui.login.LoginActivity
import net.peercoin.peercoinwallet.ui.login.pin.PinFragment


@Suppress("DEPRECATION")
class FingerDialogFragment : DialogFragment(), FingerAuthenticationHandler.FingerInterface {

    companion object {
        val TAG: String = FingerDialogFragment::class.java.simpleName
        fun newInstance() = FingerDialogFragment()
    }

    private lateinit var fingerPinAdapter: FingerPinAdapter
    private lateinit var viewModel: FingerDialogViewModel
    private lateinit var introFadeRunnable: Runnable
    private lateinit var screenShowRunnable: Runnable
    private lateinit var fingerprintManager: FingerprintManager
    private lateinit var keyguardManager: KeyguardManager
    private val handler = Handler()

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
        tvCancel.setOnClickListener {
            handler.removeCallbacks(introFadeRunnable)
            handler.removeCallbacks(screenShowRunnable)
            dismiss()
        }

        @RequiresApi(Build.VERSION_CODES.M)
        if (checkFinger()) {
            createFingerAuthentication()
        }
    }

    private fun setupRecyclerView() {
        ivClose.setOnClickListener { activity!!.finish() }
        fingerPinAdapter = FingerPinAdapter(PinFragment.PIN_SIZE)
        rvPin.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvPin.adapter = fingerPinAdapter
    }


    private fun replaceIntroWithPin() {

        introFadeRunnable = Runnable { llIntroMessage.animate().alpha(0f).duration = 300 }
        screenShowRunnable = Runnable {
            val imm = etPinConfirm.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etPinConfirm, SHOW_IMPLICIT)
            dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            flFingerContent.visibility = View.VISIBLE
            flFingerContent.animate().alpha(1f).duration = 500
            setupEditText()
        }

        handler.postDelayed(introFadeRunnable, 2000)
        handler.postDelayed(screenShowRunnable, 2300)
    }

    private fun showIntroMessage() {
        handler.postDelayed({
            llIntroMessage.animate().alpha(1f).duration = 125
            llIntroMessage.animate().scaleX(1f).scaleY(1f).duration = 250
        }, 400)
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
                    if (pass == (activity as LoginActivity).getDecryptedText()) {
                        completeInput()
                    } else {
                        shakeForError()
                        resetAdapter()
                    }
                }
            }
        })

        etPinConfirm.isFocusableInTouchMode = true

        val listener = object : EditTextKeyEvent.KeyboardListener {
            override fun enterPressed() {

            }

            override fun backPressed() {
                //Check if keyboard should be collapsible
            }
        }
        etPinConfirm.setOnKeyboardListener(listener)
    }

    fun completeInput() {
        handler.postDelayed({
            finishFingerAuth()
        }, 400)
    }

    private fun finishFingerAuth() {
        val imm = etPinConfirm.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etPinConfirm.windowToken, 0)
        dismiss()
        (activity as LoginActivity).stepOnePaperKey()
    }


    private fun shakeForError() {
        val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.shake)
        rvPin.startAnimation(animation)
    }

    private fun shakeScreen(){
        val animation = AnimationUtils.loadAnimation(context, R.anim.shakescreen)
        flFingerContent.startAnimation(animation)
    }

    private fun resetAdapter() {
        Handler().postDelayed({
            fingerPinAdapter.reset()
            etPinConfirm.text = null
        }, 400)
    }

    private fun checkFinger(): Boolean {

        var counter = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager = activity!!.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
            keyguardManager = activity!!.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager


            if (!this.fingerprintManager.isHardwareDetected) {
                Toast.makeText(activity, R.string.dev_not_supp, Toast.LENGTH_LONG).show()
                ++counter
            }
            if (!this.fingerprintManager.hasEnrolledFingerprints()) {
                //There is no fingerprint on mobile detected
                Toast.makeText(activity, R.string.no_enrolled_fp, Toast.LENGTH_LONG).show()
                ++counter
            }
            if (!this.keyguardManager.isDeviceSecure) {
                //Device is not locked with pin
                Toast.makeText(activity, R.string.dev_not_secure, Toast.LENGTH_LONG).show()
                ++counter
            }

        }
        return counter == 0
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun createFingerAuthentication() {
        var fingerAuthentication = FingerAuthentication(fingerprintManager, this)
        fingerAuthentication.initializeKey()

        if (fingerAuthentication.initializeCipher()) {
            fingerAuthentication.setAndStart()
        }
    }


    //Fingerprint authentication area
   override fun onAuthError(message: String) {

    }

    override fun onAuthHelp(message: String) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }

    override fun onAuthFailed(message: String) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
        shakeScreen()
    }

    override fun onAuthSucceed() {
        completeInput()
    }

}
