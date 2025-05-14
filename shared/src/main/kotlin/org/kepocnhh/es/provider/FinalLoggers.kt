package org.kepocnhh.es.provider

internal class FinalLoggers : Logger.Factory {
    override fun create(tag: String): Logger {
        return DesktopLogger(tag = tag)
    }
}

private class DesktopLogger(
    private val tag: String,
) : Logger {
    override fun debug(message: String) {
        System.out.println("debug:$tag: $message")
    }

    override fun warning(message: String) {
        System.err.println("warning:$tag: $message")
    }
}
