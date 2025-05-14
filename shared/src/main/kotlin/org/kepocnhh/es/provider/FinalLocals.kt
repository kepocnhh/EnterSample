package org.kepocnhh.es.provider

import org.kepocnhh.es.entity.Keys
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
            TODO("FinalLocals:keys:get")
        }
        set(value) {
            TODO("FinalLocals:keys:set($value)")
        }
}
