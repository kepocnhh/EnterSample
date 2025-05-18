package org.kepocnhh.es

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.kepocnhh.es.module.router.RouterScreen

fun main() {
    Env.appId = "pf1"
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "EnterSample-${Env.appId}",
        ) {
            RouterScreen(color = Color.White)
        }
    }
}
