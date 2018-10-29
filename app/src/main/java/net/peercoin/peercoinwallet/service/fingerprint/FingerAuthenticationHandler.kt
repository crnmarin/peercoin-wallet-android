@file:Suppress("DEPRECATION")

package net.peercoin.peercoinwallet.service.fingerprint

import android.annotation.TargetApi
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal

@TargetApi(Build.VERSION_CODES.M)
class FingerAuthenticationHandler(private var listener: FingerInterface) : FingerprintManager.AuthenticationCallback() {

    private lateinit var cancellationSignal: CancellationSignal

    fun startAuthentication(manager: FingerprintManager, cryptoObject: FingerprintManager.CryptoObject) {
        cancellationSignal = CancellationSignal()
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null)
    }

    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence) {
        val message = "Authentication error\n$errString"
        listener.onAuthError(message)
    }

    override fun onAuthenticationFailed() {
        val message = "Authentication failed\n"
        listener.onAuthFailed(message)
    }

    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence) {
        val message = "Authentication help\n$helpString"
        listener.onAuthHelp(message)
    }

    override fun onAuthenticationSucceeded(
            result: FingerprintManager.AuthenticationResult) {
        listener.onAuthSucceed()
    }

    interface FingerInterface {
        fun onAuthError(message: String)
        fun onAuthHelp(message: String)
        fun onAuthFailed(message: String)
        fun onAuthSucceed()
    }

}