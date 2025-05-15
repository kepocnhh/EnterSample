package org.kepocnhh.es.provider

import java.security.KeyStore
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.PBEParameterSpec
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
//        val keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
//        val keyFactory = SecretKeyFactory.getInstance("PBEWITHHMACSHA256ANDAES_256")
        val keyFactory = SecretKeyFactory.getInstance("PBKDF2WITHHMACSHA256")
        val salt = "0196d52b-a0c8-765e-a6de-03e48316ef64".toByteArray() // todo
        val iterationCount = 600_000
        val keySpec = PBEKeySpec(password, salt, iterationCount, 256) // todo
        return keyFactory.generateSecret(keySpec)
    }

    override fun encrypt(key: SecretKey, decrypted: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES")
//        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
//        val cipher = Cipher.getInstance("PBKDF2WithHmacSHA256")
//        val cipher = Cipher.getInstance("PBEWITHHMACSHA256ANDAES_256")
//        val cipher = Cipher.getInstance("AES_256/CBC/NOPADDING")
//        cipher.init(Cipher.ENCRYPT_MODE, key)
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key.encoded, "AES"))
//        val salt = "0196d52b-a0c8-765e-a6de-03e48316ef64".toByteArray() // todo
//        val iv = "527d0fa2-7208-4a48-a8c9-66bc58811a26".toByteArray().take(16).toByteArray() // todo
//        val params = IvParameterSpec(iv)
//        val spec = PBEParameterSpec(salt, 1, params)
//        cipher.init(Cipher.ENCRYPT_MODE, key, spec)
        return cipher.doFinal(decrypted)
    }

    override fun decrypt(key: SecretKey, encrypted: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES")
//        val cipher = Cipher.getInstance("PBEWITHHMACSHA256ANDAES_256")
//        val salt = "0196d52b-a0c8-765e-a6de-03e48316ef64".toByteArray() // todo
//        val iv = "527d0fa2-7208-4a48-a8c9-66bc58811a26".toByteArray().take(16).toByteArray() // todo
//        val params = IvParameterSpec(iv)
//        val spec = PBEParameterSpec(salt, 1, params)
//        cipher.init(Cipher.DECRYPT_MODE, key, spec)
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key.encoded, "AES"))
        return cipher.doFinal(encrypted)
    }
}
