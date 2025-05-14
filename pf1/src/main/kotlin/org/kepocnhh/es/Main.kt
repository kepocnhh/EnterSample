package org.kepocnhh.es

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.kepocnhh.es.module.router.RouterScreen

fun main() {
    application {
        Window(onCloseRequest = ::exitApplication) {
            RouterScreen()
        }
    }
}
