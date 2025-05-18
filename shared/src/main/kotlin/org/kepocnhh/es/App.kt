package org.kepocnhh.es

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.Dispatchers
import org.kepocnhh.es.provider.Contexts
import org.kepocnhh.es.provider.FinalAssets
import org.kepocnhh.es.provider.FinalLocals
import org.kepocnhh.es.provider.FinalLoggers
import org.kepocnhh.es.provider.FinalSecrets
import org.kepocnhh.es.provider.FinalTimes
import org.kepocnhh.es.provider.Injection
import org.kepocnhh.es.provider.Logger
import org.kepocnhh.es.provider.Sessions
import sp.kx.logics.Logics
import sp.kx.logics.LogicsFactory
import sp.kx.logics.LogicsProvider
import sp.kx.logics.contains
import sp.kx.logics.get
import sp.kx.logics.remove

internal object App {
    private var _injection: Injection? = null
    val injection: Injection get() = checkNotNull(_injection) { "No injection!" }

    init {
        val loggers: Logger.Factory = FinalLoggers()
        _injection = Injection(
            contexts = Contexts(
                main = Dispatchers.Main,
                default = Dispatchers.Default,
            ),
            loggers = loggers,
            locals = FinalLocals(
                namespace = "org.kepocnhh.es",
                appId = Env.appId ?: error("No app ID!")
            ),
            sessions = Sessions(privateKey = null),
            secrets = FinalSecrets(),
            assets = FinalAssets(),
            times = FinalTimes(),
        )
    }

    private val _logicsProvider = LogicsProvider(
        factory = object : LogicsFactory {
            override fun <T : Logics> create(type: Class<T>): T {
                return type
                    .getConstructor(Injection::class.java)
                    .newInstance(injection)
            }
        },
    )

    @Composable
    inline fun <reified T : Logics> logics(label: String = T::class.java.name): T {
        val (contains, logic) = synchronized(App::class.java) {
            remember { _logicsProvider.contains<T>(label = label) } to _logicsProvider.get<T>(label = label)
        }
        DisposableEffect(Unit) {
            onDispose {
                synchronized(App::class.java) {
                    if (!contains) _logicsProvider.remove<T>(label = label)
                }
            }
        }
        return logic
    }
}
