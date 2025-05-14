package org.kepocnhh.es.entity

import kotlin.time.Duration

class EnterSalt(
    val time: Duration,
    val encryptedSalt: ByteArray,
    val signature: ByteArray,
)
