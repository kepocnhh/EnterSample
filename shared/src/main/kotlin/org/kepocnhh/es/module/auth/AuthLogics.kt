package org.kepocnhh.es.module.auth

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import org.kepocnhh.es.entity.Keys
import org.kepocnhh.es.provider.Injection
import sp.kx.bytes.toHEX
import sp.kx.logics.Logics
import java.security.PublicKey
import java.security.Security

internal class AuthLogics(
    private val injection: Injection,
) : Logics(injection.contexts.main) {
    sealed interface Event {
        class OnAuth(val result: Result<Pair<Keys, ByteArray>>) : Event
    }

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private val logger = injection.loggers.create("[Auth]")

    private fun getKeys(
        publicKey: PublicKey,
        privateKey: ByteArray,
        password: CharArray,
    ): Keys {
        setOf(
            "SecretKeyFactory",
            "Cipher",
        ).forEach { serviceName ->
            logger.debug("service: $serviceName") // todo
            Security.getAlgorithms(serviceName)?.forEachIndexed { index, it ->
//                logger.debug("$index] $it") // todo
            }
        }
        val secretKey = injection.secrets.getSecretKey(password = password)
        logger.debug("secret:key: ${injection.secrets.sha256(secretKey.encoded).toHEX()}")
        return Keys(
            publicKey = publicKey.encoded,
            privateKeyEncrypted = injection.secrets.encrypt(secretKey, privateKey),
        )
    }

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
                getKeys(
                    publicKey = publicKey,
                    privateKey = privateKey,
                    password = password.toHEX().toCharArray(),
                ) to privateKey
            }
        }
        _events.emit(Event.OnAuth(result))
    }
}
