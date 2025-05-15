package org.kepocnhh.es.provider

import java.security.KeyStore
import javax.crypto.SecretKey

interface Secrets {
    fun toKeyStore(encoded: ByteArray, password: CharArray): KeyStore
    fun sha256(encoded: ByteArray): ByteArray
    fun getSecretKey(password: CharArray): SecretKey
    fun encrypt(key: SecretKey, decrypted: ByteArray): ByteArray
    fun decrypt(key: SecretKey, encrypted: ByteArray): ByteArray
}
