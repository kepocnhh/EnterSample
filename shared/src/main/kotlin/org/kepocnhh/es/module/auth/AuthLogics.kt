package org.kepocnhh.es.module.auth

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import org.kepocnhh.es.entity.AuthorizedPackage
import org.kepocnhh.es.entity.Keys
import org.kepocnhh.es.provider.Injection
import sp.kx.bytes.readInt
import sp.kx.bytes.toHEX
import sp.kx.logics.Logics
import java.util.UUID

internal class AuthLogics(
    private val injection: Injection,
) : Logics(injection.contexts.main) {
    sealed interface Event {
        class OnAuth(val result: Result<Pair<Keys, ByteArray>>) : Event
        class OnEnter(val result: Result<Unit>) : Event
    }

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private val logger = injection.loggers.create("[Auth]")

    fun auth(
        file: String,
        keyStorePassword: String,
        alias: String,
        pin: String,
    ) = launch {
        val result = withContext(injection.contexts.default) {
            runCatching {
                if (keyStorePassword.isBlank()) error("KeyStore password is blank!")
                if (pin.isBlank()) error("PIN is blank!")
                logger.debug("read \"$file\"...")
                val keyStore = injection.assets.getAsset(name = file).use {
                    logger.debug("load key store...")
                    injection.secrets.toKeyStore(it.readBytes(), password = keyStorePassword.toCharArray())
                }
                val privateKey = keyStore.getKey(alias, keyStorePassword.toCharArray())?.encoded ?: error("No \"$alias\"!")
                logger.debug("private:key: ${injection.secrets.sha256(privateKey).toHEX()}")
                val certificate = keyStore.getCertificate(alias)
                logger.debug("certificate: ${injection.secrets.sha256(certificate.encoded).toHEX()}")
                val publicKey = certificate.publicKey
                logger.debug("public:key: ${injection.secrets.sha256(publicKey.encoded).toHEX()}")
                val password = injection.secrets.sha256(pin.toByteArray())
                val secretKey = injection.secrets.getSecretKey(password = password.toHEX().toCharArray())
                logger.debug("secret:key: ${injection.secrets.sha256(secretKey.encoded).toHEX()}")
                val keys = Keys(
                    id = UUID.randomUUID(), // todo
                    publicKey = publicKey.encoded,
                    privateKeyEncrypted = injection.secrets.encrypt(secretKey, privateKey),
                )
                keys to privateKey
            }
        }
        _events.emit(Event.OnAuth(result = result))
    }

    fun enter(authorizedPackage: AuthorizedPackage) = launch {
        logger.debug("on enter: ${authorizedPackage.id}")
        val result = withContext(injection.contexts.default) {
            runCatching {
                logger.debug("public:key: ${injection.secrets.sha256(authorizedPackage.publicKey).toHEX()}")
                val file = injection.dirs.files.resolve("${authorizedPackage.namespace}-keys-${authorizedPackage.id}")
                val bytes = file.readBytes()
                var index = 0
                index += 4
                index += authorizedPackage.publicKey.size
                val privateKeyEncrypted = ByteArray(bytes.readInt(index = index))
                index += 4
                System.arraycopy(bytes, index, privateKeyEncrypted, 0, privateKeyEncrypted.size)
                injection.locals.keys = Keys(
                    id = authorizedPackage.id,
                    publicKey = authorizedPackage.publicKey,
                    privateKeyEncrypted = privateKeyEncrypted,
                )
            }
        }
        _events.emit(Event.OnEnter(result = result))
    }
}
