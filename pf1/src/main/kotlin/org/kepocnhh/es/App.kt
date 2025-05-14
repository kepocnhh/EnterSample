package org.kepocnhh.es

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() {
    application {
        Window(onCloseRequest = ::exitApplication) {
            // todo
        }
    }
}
