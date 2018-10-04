package net.peercoin.peercoinwallet.ui.login

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_login.*
import net.peercoin.peercoinwallet.R
import net.peercoin.peercoinwallet.service.pin.DeCryptor
import net.peercoin.peercoinwallet.service.pin.EnCryptor
import net.peercoin.peercoinwallet.ui.login.fingerprint.FingerDialogFragment
import net.peercoin.peercoinwallet.ui.login.paper_key.intro.PaperKeyIntroFragment
import net.peercoin.peercoinwallet.ui.login.paper_key.stepOne.PaperKeyStepOneFragment
import net.peercoin.peercoinwallet.ui.login.paper_key.stepTwo.PaperKeyStepTwoFragment
import net.peercoin.peercoinwallet.ui.login.pin.PinFragment


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        supportFragmentManager.beginTransaction()
                .replace(R.id.flContent, PinFragment.newInstance(), PinFragment.TAG)
                .addToBackStack(null)
                .commit()

        ivBack.setOnClickListener { finish() }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentByTag(PinFragment.TAG) == null) {
            if (supportFragmentManager.backStackEntryCount == 1)
                finish()
            else super.onBackPressed()
        }
    }

    fun registerSuccessful(pass: String) {
        //Do PIN storage
        val encryptor = EnCryptor()
        val decryptor = DeCryptor()

        encryptText(encryptor, pass)
        decryptText(decryptor,encryptor)

        Handler().postDelayed({
            if (supportFragmentManager.backStackEntryCount > 0)
                supportFragmentManager.popBackStackImmediate()

            supportFragmentManager.beginTransaction()
                    .replace(R.id.flContent, PaperKeyIntroFragment.newInstance(), PaperKeyIntroFragment.TAG)
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit()

            ivBack.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_close_accent_24dp))
            tvHeaderTitle.text = getString(R.string.paper_key)
        }, 2000L)
    }

    private val fingerDialogFragment: FingerDialogFragment
        get() {
            val fingerDialog = FingerDialogFragment.newInstance()
            return fingerDialog
        }

    fun writePaperKey() {
        //Should be fingerprint authentication

        val fingerDialog = fingerDialogFragment
        fingerDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog_theme)
        fingerDialog.show(supportFragmentManager, FingerDialogFragment.TAG)
    }

    fun stepOnePaperKey() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.flContent, PaperKeyStepOneFragment.newInstance(), PaperKeyStepOneFragment.TAG)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
    }

    fun finishPaperKey(randomWords: HashMap<Int, String>) {
        //Step two of authentication
        supportFragmentManager.beginTransaction()
                .replace(R.id.flContent, PaperKeyStepTwoFragment.newInstance(randomWords), PaperKeyStepTwoFragment.TAG)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
    }

    private fun encryptText(encryptor: EnCryptor, pass: String) {
        val encryptedText: ByteArray = encryptor.encryptText("AndroidKeyStore", pass)
        Log.d("EncryptComplete", encryptedText.toString())

    }

    private fun decryptText(decryptor: DeCryptor, encryptor: EnCryptor) {
        val decryptedText: String = decryptor.decryptData("AndroidKeyStore", encryptor.getEncryption(),
                encryptor.getIv())
        Log.d("DecryptComplete", decryptedText)
    }

}
