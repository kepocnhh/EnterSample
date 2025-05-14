package org.kepocnhh.es.provider

import java.security.KeyStore

interface Secrets {
    fun toKeyStore(encoded: ByteArray, password: CharArray): KeyStore
    fun sha256(encoded: ByteArray): ByteArray
}
