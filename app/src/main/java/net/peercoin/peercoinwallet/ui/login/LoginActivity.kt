package net.peercoin.peercoinwallet.ui.login

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
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

    private var decryptedText: String = ""
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.flContent, PinFragment.newInstance(), PinFragment.TAG)
                .addToBackStack(null)
                .commit()

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
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
        decryptText(decryptor, encryptor)

        Handler().postDelayed({
            if (supportFragmentManager.backStackEntryCount > 0)
                supportFragmentManager.popBackStackImmediate()

            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.flContent, PaperKeyIntroFragment.newInstance(), PaperKeyIntroFragment.TAG)
                    .addToBackStack(null)
                    .commit()

            ivBack.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_close_accent_24dp))
            tvHeaderTitle.text = getString(R.string.paper_key)
        }, 2000L)
    }

    fun writePaperKey() {

        val fingerDialog = FingerDialogFragment.newInstance()
        fingerDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog_theme)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        fingerDialog.show(supportFragmentManager, FingerDialogFragment.TAG)
    }

    fun stepOnePaperKey() {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .addToBackStack(null)
                .replace(R.id.flContent, PaperKeyStepOneFragment.newInstance(), PaperKeyStepOneFragment.TAG)
                .commit()
    }

    fun finishPaperKey(randomWords: HashMap<Int, String>) {
        //Step two of authentication
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.flContent, PaperKeyStepTwoFragment.newInstance(randomWords), PaperKeyStepTwoFragment.TAG)
                .addToBackStack(null)
                .commit()
    }

    private fun encryptText(encryptor: EnCryptor, pass: String) {
        val encryptedText: ByteArray = encryptor.encryptText("AndroidKeyStore", pass)
        Log.d("EncryptComplete", encryptedText.toString())
    }

    private fun decryptText(decryptor: DeCryptor, encryptor: EnCryptor) {
        this.decryptedText = decryptor.decryptData("AndroidKeyStore", encryptor.getEncryption(),
                encryptor.getIv())
        Log.d("DecryptComplete", this.decryptedText)
        this.sharedPreferences.edit().putString("KEY PASSWORD", this.decryptedText).apply()
    }

    fun getDecryptedText(): String {
        return this.decryptedText
    }

}