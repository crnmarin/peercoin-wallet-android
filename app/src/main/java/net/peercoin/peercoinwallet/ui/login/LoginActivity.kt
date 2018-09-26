package net.peercoin.peercoinwallet.ui.login

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_login.*
import net.peercoin.peercoinwallet.R
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

    fun registerSuccessful() {
        //Do PIN storage

        Handler().postDelayed({
            if (supportFragmentManager.backStackEntryCount > 0)
                supportFragmentManager.popBackStackImmediate()

            supportFragmentManager.beginTransaction()
                    .replace(R.id.flContent, PaperKeyIntroFragment.newInstance(), PaperKeyIntroFragment.TAG)
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()

            ivBack.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_close_black_24dp))
            tvHeaderTitle.text = getString(R.string.paper_key)
        }, 2000L)
    }

    fun writePaperKey() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.flContent, PaperKeyStepOneFragment.newInstance(), PaperKeyStepOneFragment.TAG)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
    }

    fun finishPaperKey(randomWords: HashMap<Int, String>) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.flContent, PaperKeyStepTwoFragment.newInstance(randomWords), PaperKeyStepTwoFragment.TAG)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
    }
}
