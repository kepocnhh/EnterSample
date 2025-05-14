package org.kepocnhh.es.provider

import java.io.InputStream

interface Assets {
    fun getAsset(name: String): InputStream
}
