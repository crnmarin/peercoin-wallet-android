package net.peercoin.peercoinwallet.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import net.peercoin.peercoinwallet.R
import net.peercoin.peercoinwallet.ui.login.pin.PinFragment


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportFragmentManager.beginTransaction().add(R.id.flContent, PinFragment.newInstance(), PinFragment.TAG).commit()
        ivBack.setOnClickListener { onBackPressed() }

    }

    fun registerSuccessful() {
        //Do PIN storage
    }
}
