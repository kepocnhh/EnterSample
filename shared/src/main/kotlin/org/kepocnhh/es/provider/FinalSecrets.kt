package org.kepocnhh.es.provider

import java.security.KeyStore
import java.security.MessageDigest

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
}
