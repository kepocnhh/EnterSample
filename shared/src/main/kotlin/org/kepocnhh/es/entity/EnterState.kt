package org.kepocnhh.es.entity

import java.security.PublicKey
import java.util.UUID
import javax.crypto.SecretKey

class EnterState(
    val id: UUID,
    val secretKey: SecretKey,
    val publicKey: PublicKey,
)
