package org.kepocnhh.es

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.kepocnhh.es.module.router.RouterScreen

fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "EnterSample-pf2",
        ) {
            RouterScreen()
        }
    }
}
