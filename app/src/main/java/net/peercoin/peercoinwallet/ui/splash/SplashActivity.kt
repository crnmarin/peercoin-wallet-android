package net.peercoin.peercoinwallet.ui.splash

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.transition.Explode
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.peercoin.peercoinwallet.R
import net.peercoin.peercoinwallet.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setAnimations()
        btnCreate.setOnClickListener { openCreateWallet() }

    }

    private fun openCreateWallet() {
        intent = Intent(this, LoginActivity::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Apply activity transition
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        } else {
            // Swap without transition
            startActivity(intent)
        }
    }

    fun setAnimations() {

        llRegister.y = 200f
        //fade out logo

        val handler = Handler()

        handler.postDelayed({
            ivLogo.animate().alpha(0.0f).duration = 400
        }, 400)

        handler.postDelayed({
            //Delayed animation to new height
            val newHeight = ivLogo.y - 100f
            ivLogoText.animate()
                    .y(newHeight)
                    .setDuration(500)
                    .withEndAction { ivLogoText.y = newHeight }

            llRegister.animate().translationY(0f).alpha(1f).duration = 400
        }, 800)
    }

}
