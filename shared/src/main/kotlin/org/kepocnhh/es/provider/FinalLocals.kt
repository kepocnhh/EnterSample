package org.kepocnhh.es.provider

import org.kepocnhh.es.entity.Keys
import sp.kx.bytes.readInt
import sp.kx.bytes.write
import java.io.File

internal class FinalLocals : Locals {
    private val dir = File(System.getProperty("user.home")).resolve(".tmp")

    init {
        dir.mkdirs()
    }

    override var keys: Keys?
        get() {
            val file = dir.resolve("keys")
            if (!file.exists()) return null
            val bytes = file.readBytes()
            var index = 0
            val publicKey = ByteArray(bytes.readInt(index = index))
            index += 4
            System.arraycopy(bytes, index, publicKey, 0, publicKey.size)
            index += publicKey.size
            val privateKeyEncrypted = ByteArray(bytes.readInt(index = index))
            index += 4
            System.arraycopy(bytes, index, privateKeyEncrypted, 0, privateKeyEncrypted.size)
            return Keys(
                publicKey = publicKey,
                privateKeyEncrypted = privateKeyEncrypted,
            )
        }
        set(value) {
            val file = dir.resolve("keys")
            if (value == null) {
                file.delete()
            } else {
                val bytes = ByteArray(4 + value.publicKey.size + 4 + value.privateKeyEncrypted.size)
                var index = 0
                bytes.write(index = index, value.publicKey.size)
                index += 4
                System.arraycopy(value.publicKey, 0, bytes, index, value.publicKey.size)
                index += value.publicKey.size
                bytes.write(index = index, value.privateKeyEncrypted.size)
                index += 4
                System.arraycopy(value.privateKeyEncrypted, 0, bytes, index, value.privateKeyEncrypted.size)
                file.writeBytes(bytes)
            }
        }
}
