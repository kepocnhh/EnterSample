package org.kepocnhh.es.provider

import java.security.KeyStore
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

internal class FinalSecrets : Secrets {
    override fun toKeyStore(encoded: ByteArray, password: CharArray): KeyStore {
        val keyStore = KeyStore.getInstance("PKCS12")
        keyStore.load(encoded.inputStream(), password)
        return keyStore
    }

    override fun sha256(encoded: ByteArray): ByteArray {
        val md = MessageDigest.getInstance("SHA256")
        return md.digest(encoded)
    }

    override fun getSecretKey(password: CharArray): SecretKey {
//        val keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val keyFactory = SecretKeyFactory.getInstance("PBEWITHHMACSHA256ANDAES_256")
        val salt = "salt".toByteArray() // todo
        val keySpec = PBEKeySpec(password, salt, 1, 256) // todo
        return keyFactory.generateSecret(keySpec)
    }

    override fun encrypt(key: SecretKey, decrypted: ByteArray): ByteArray {
//        val cipher = Cipher.getInstance("AES")
//        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val cipher = Cipher.getInstance("PBEWITHHMACSHA256ANDAES_256")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return cipher.doFinal(decrypted)
    }
}
