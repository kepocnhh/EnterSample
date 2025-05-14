package org.kepocnhh.es.provider

import kotlin.coroutines.CoroutineContext

data class Contexts(
    val main: CoroutineContext,
    val default: CoroutineContext,
)
