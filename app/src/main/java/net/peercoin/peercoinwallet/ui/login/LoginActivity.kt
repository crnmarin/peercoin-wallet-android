package net.peercoin.peercoinwallet.ui.login

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_login.*
import net.peercoin.peercoinwallet.R
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
                .setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
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

    fun registerSuccessful() {
        //Do PIN storage

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
        transaction.setCustomAnimations(R.anim.from_left_to_right, R.anim.fade_out)
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
}
