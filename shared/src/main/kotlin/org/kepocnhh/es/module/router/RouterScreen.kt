package org.kepocnhh.es.module.router

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.kepocnhh.es.App
import org.kepocnhh.es.module.auth.AuthScreen
import org.kepocnhh.es.module.enter.EnterScreen
import org.kepocnhh.es.module.main.MainScreen

@Composable
fun RouterScreen(color: Color) {
    val logics = App.logics<RouterLogics>()
    val state = logics.states.collectAsState().value
    LaunchedEffect(Unit) {
        if (state == null) logics.requestState()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color),
    ) {
        when (state) {
            is RouterLogics.State.Keys -> {
                if (state.authorized) {
                    MainScreen(
                        publicKey = state.publicKey,
                        onLock = logics::lock,
                    )
                } else {
                    EnterScreen(
                        onEnter = { privateKey: ByteArray, _ ->
                            logics.enter(privateKey = privateKey)
                        },
                        onExit = logics::exit,
                    )
                }
            }
            RouterLogics.State.NoKeys -> {
                AuthScreen(
                    onAuth = logics::auth,
                    onEnter = logics::requestState,
                )
            }
            null -> {
                // noop
            }
        }
    }
}

