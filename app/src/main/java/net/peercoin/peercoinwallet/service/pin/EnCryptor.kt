package net.peercoin.peercoinwallet.service.pin

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import java.io.IOException
import java.security.*
import javax.crypto.*

class EnCryptor {
    private val TRANSFORMATION: String = "AES/GCM/NoPadding"
    private val ANDROID_KEY_STORE: String = "AndroidKeyStore"

    private lateinit var encryption: ByteArray
    private lateinit var ivs: ByteArray

    @Throws(UnrecoverableEntryException::class, NoSuchAlgorithmException::class, KeyStoreException::class,
            NoSuchProviderException::class, NoSuchPaddingException::class, InvalidKeyException::class,
            IOException::class, InvalidAlgorithmParameterException::class, SignatureException::class,
            BadPaddingException::class, IllegalBlockSizeException::class)
    fun encryptText(alias: String, textToEncrypt: String): ByteArray {
        val cipher: Cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias))

        ivs = cipher.iv

        this.encryption = cipher.doFinal(textToEncrypt.toByteArray(charset("UTF-8")))

        return this.encryption
    }

    private fun getSecretKey(alias: String): SecretKey {
        val keyGenerator: KeyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
                ANDROID_KEY_STORE)
        val keygenParameterSpec: KeyGenParameterSpec = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            KeyGenParameterSpec.Builder(alias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT).
                    setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).build()

        } else {
            TODO("VERSION.SDK_INT < M")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyGenerator.init(keygenParameterSpec)
        }
        return keyGenerator.generateKey()
    }

    fun getEncryption(): ByteArray {
        return this.encryption
    }

    fun getIv(): ByteArray {
        return this.ivs
    }


}