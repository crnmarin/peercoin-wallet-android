package net.peercoin.peercoinwallet.service.fingerprint

import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.security.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey

@Suppress("DEPRECATION")
class FingerAuthentication(private var fpManager: FingerprintManager, private var listener: FingerAuthenticationHandler.FingerInterface) {

    private val KEY_NAME: String = "FingerKey"
    private var ANDROID_KEY_STORE: String = "AndroidKeyStore"
    private lateinit var cipher: Cipher
    private lateinit var keyStore: KeyStore
    private lateinit var keyGenerator: KeyGenerator
    private lateinit var cryptoObject: FingerprintManager.CryptoObject

    fun initializeKey() {
        keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
        keyStore.load(null)

        keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // these are properties of our keyGenerator
            val builder: KeyGenParameterSpec.Builder = KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)

            keyGenerator.init(builder.build())
            keyGenerator.generateKey() // generate our secret key
        }
    }

    //Cipher is used for encryption/decryption
    fun initializeCipher(): Boolean {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to get Cipher", e)
        } catch (e: NoSuchPaddingException) {
            throw RuntimeException("Failed to get Cipher", e)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                keyStore.load(null)
                val secretKey: SecretKey = keyStore.getKey(KEY_NAME, null) as SecretKey
                cipher.init(Cipher.ENCRYPT_MODE, secretKey)
                return true
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException("Failed to init Cipher", e)
            } catch (e: InvalidKeyException) {
                throw RuntimeException("Failed to init Cipher", e)
            } catch (e: UnrecoverableKeyException) {
                throw RuntimeException("Failed to init Cipher", e)

            } catch (e: KeyPermanentlyInvalidatedException) {
                return false
            } catch (e: KeyStoreException) {
                throw RuntimeException("Failed to init Cipher", e)
            }


        }
        return false
    }

    //use of cryptoObject is to know is there any new fingerprints on device since last log
    @RequiresApi(Build.VERSION_CODES.M)
    fun setAndStart() {
        cryptoObject = FingerprintManager.CryptoObject(cipher)
        val helper = FingerAuthenticationHandler(listener)
        helper.startAuthentication(fpManager, cryptoObject)
    }


}