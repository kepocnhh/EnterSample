package org.kepocnhh.es.provider

data class Injection(
    val contexts: Contexts,
    val loggers: Logger.Factory,
    val locals: Locals,
    val sessions: Sessions,
    val secrets: Secrets,
    val assets: Assets,
    val times: Times,
    val dirs: Dirs,
)
