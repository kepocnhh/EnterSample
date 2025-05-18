package org.kepocnhh.es.provider

import org.kepocnhh.es.entity.Keys
import sp.kx.bytes.readInt
import sp.kx.bytes.readUUID
import sp.kx.bytes.toByteArray
import sp.kx.bytes.write

internal class FinalLocals(
    private val namespace: String,
    private val appId: String,
    dirs: Dirs,
) : Locals {
    private val files = dirs.files

    init {
        files.mkdirs()
    }

    override var keys: Keys?
        get() {
            val envs = files.resolve("$namespace-$appId")
            if (!envs.exists()) return null
            val id = envs.readBytes().readUUID()
            val file = files.resolve("$namespace-keys-$id")
            if (!file.exists()) TODO("no file $file")
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
                id = id,
                publicKey = publicKey,
                privateKeyEncrypted = privateKeyEncrypted,
            )
        }
        set(value) {
            if (value == null) {
                val envs = files.resolve("$namespace-$appId")
                envs.delete()
                return
            }
            val file = files.resolve("$namespace-keys-${value.id}")
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
            val envs = files.resolve("$namespace-$appId")
            envs.writeBytes(value.id.toByteArray())
        }
}
