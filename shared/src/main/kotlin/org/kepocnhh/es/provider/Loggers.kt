package org.kepocnhh.es.provider

interface Logger {
    interface Factory {
        fun create(tag: String): Logger
    }

    fun debug(message: String)
    fun warning(message: String)
}
