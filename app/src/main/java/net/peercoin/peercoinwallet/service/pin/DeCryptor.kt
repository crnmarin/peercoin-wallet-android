package net.peercoin.peercoinwallet.service.pin

import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.*
import javax.crypto.spec.GCMParameterSpec

class DeCryptor() {
    private var TRANSFORMATION : String = "AES/GCM/NoPadding"
    private var ANDROID_KEY_STORE : String = "AndroidKeyStore"

    private lateinit var keyStore : KeyStore

    //@Throws(CertificateException::class,NoSuchAlgorithmException::class,KeyStoreException::class, IOException::class)
    init {
        initKeyStore()
    }

    @Throws(KeyStoreException::class,CertificateException::class,NoSuchAlgorithmException::class,
            IOException::class)
    private fun initKeyStore(){
        keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
        keyStore.load(null)
    }

    @Throws(NoSuchAlgorithmException::class)
    private fun getSecretKey(alias : String) : SecretKey{
        var secretKeyEntry : KeyStore.SecretKeyEntry = keyStore.getEntry(alias,null) as KeyStore.SecretKeyEntry

        return secretKeyEntry.secretKey
    }

    @Throws(UnrecoverableEntryException::class,NoSuchAlgorithmException::class,KeyStoreException::class,
            NoSuchProviderException::class,NoSuchPaddingException::class,InvalidKeyException::class,IOException::class,
            BadPaddingException::class,IllegalBlockSizeException::class,InvalidAlgorithmParameterException::class)
    fun decryptData(alias : String,encryptedData : ByteArray,encryptionIv : ByteArray) : String{

        val cipher : Cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(128,encryptionIv)
        cipher.init(Cipher.DECRYPT_MODE,getSecretKey(alias),spec)

        val decodedData : ByteArray = cipher.doFinal(encryptedData)

        return String(decodedData, charset("UTF-8"))
    }

}

