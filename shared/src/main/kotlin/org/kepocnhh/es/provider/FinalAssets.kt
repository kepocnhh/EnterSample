package org.kepocnhh.es.provider

import java.io.InputStream

internal class FinalAssets : Assets {
    override fun getAsset(name: String): InputStream {
        return Thread.currentThread().contextClassLoader?.getResourceAsStream(name)!!
    }
}
