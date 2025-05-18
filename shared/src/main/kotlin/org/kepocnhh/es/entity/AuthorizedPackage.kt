package org.kepocnhh.es.entity

import java.util.UUID

class AuthorizedPackage(
    val id: UUID,
    val publicKey: ByteArray,
    val namespace: String,
)
