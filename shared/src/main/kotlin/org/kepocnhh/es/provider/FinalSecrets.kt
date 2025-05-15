package org.kepocnhh.es.provider

import java.security.KeyStore
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

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
        val keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val salt = "0196d52b-a0c8-765e-a6de-03e48316ef64".toByteArray() // todo
        val iterationCount = 600_000
        val keySpec = PBEKeySpec(password, salt, iterationCount, 256) // todo
        return keyFactory.generateSecret(keySpec)
    }

    override fun encrypt(key: SecretKey, decrypted: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key.encoded, "AES"))
        return cipher.doFinal(decrypted)
    }

    override fun decrypt(key: SecretKey, encrypted: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key.encoded, "AES"))
        return cipher.doFinal(encrypted)
    }
}
