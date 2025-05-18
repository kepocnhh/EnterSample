package org.kepocnhh.es.provider

import org.kepocnhh.es.entity.Keys
import sp.kx.bytes.readInt
import sp.kx.bytes.readUUID
import sp.kx.bytes.toByteArray
import sp.kx.bytes.write
import java.io.File
import java.util.UUID

internal class FinalLocals(
    private val namespace: String,
    private val appId: String,
) : Locals {
    private val dir = File(System.getProperty("user.home")).resolve(".tmp")

    init {
        dir.mkdirs()
    }

    private fun getKeysFile(): File? {
        val envs = dir.resolve("$namespace-$appId")
        if (!envs.exists()) return null
        val id = envs.readBytes().readUUID()
        val file = dir.resolve("$namespace-keys-$id")
        if (!file.exists()) TODO("no file $file")
        return file
    }

    override var keys: Keys?
        get() {
            val file = getKeysFile() ?: return null
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
            if (value == null) {
                val envs = dir.resolve("$namespace-$appId")
                envs.delete()
                return
            }
            val id = UUID.randomUUID()
            val file = dir.resolve("$namespace-keys-$id")
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
            val envs = dir.resolve("$namespace-$appId")
            envs.writeBytes(id.toByteArray())
        }
}
