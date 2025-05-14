package org.kepocnhh.es.module.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import org.kepocnhh.es.App
import org.kepocnhh.es.module.auth.AuthScreen

@Composable
fun RouterScreen() {
    val logics = App.logics<RouterLogics>()
    val state = logics.states.collectAsState().value
    LaunchedEffect(Unit) {
        if (state == null) logics.requestState()
    }
    when (state) {
        is RouterLogics.State.Keys -> {
            TODO("RouterScreen:$state")
//            if (state.authorized) {
//                MainScreen(
//                    publicKey = state.publicKey,
//                    onLock = logics::lock,
//                )
//            } else {
//                EnterScreen(
//                    onEnter = { privateKey: ByteArray, _ ->
//                        logics.enter(privateKey = privateKey)
//                    },
//                    onExit = logics::exit,
//                )
//            }
        }
        RouterLogics.State.NoKeys -> {
            AuthScreen(
                onAuth = logics::auth,
            )
        }
        null -> {
            // noop
        }
    }
}

