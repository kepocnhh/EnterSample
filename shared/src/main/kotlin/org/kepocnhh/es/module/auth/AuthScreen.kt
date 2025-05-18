package org.kepocnhh.es.module.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.kepocnhh.es.App
import org.kepocnhh.es.entity.Keys
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun AuthScreen(
    onAuth: (Keys, ByteArray) -> Unit,
) {
    val logger = remember { App.injection.loggers.create("[Auth]") }
    val logics = App.logics<AuthLogics>()
    val popupState = remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        logics.events.collect { event ->
            when (event) {
                is AuthLogics.Event.OnAuth -> {
                    event.result.fold(
                        onSuccess = { (keys, privateKey: ByteArray) ->
                            onAuth(keys, privateKey)
                        },
                        onFailure = { error ->
                            logger.warning("auth error: $error")
                            popupState.value = "auth error: $error"
                        },
                    )
                }
            }
        }
    }
    val popup = popupState.value
    if (popup != null) {
        LaunchedEffect(popup) {
            withContext(App.injection.contexts.default) {
                delay(1.seconds)
                popupState.value = null
            }
        }
        Popup(alignment = Alignment.BottomStart) {
            BasicText(popup)
        }
    }
    val fileState = remember { mutableStateOf("a202.pkcs12") } // todo
    val keyStorePasswordState = remember { mutableStateOf("qwe202") } // todo
    val aliasState = remember { mutableStateOf("a202") } // todo
    val pinState = remember { mutableStateOf("0202") } // todo
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            BasicText("file")
            BasicTextField(
                modifier = Modifier.fillMaxWidth()
                    .height(36.dp)
                    .background(Color.LightGray)
                    .wrapContentHeight(),
                value = fileState.value,
                onValueChange = { fileState.value = it },
            )
            BasicText("password")
            BasicTextField(
                modifier = Modifier.fillMaxWidth()
                    .height(36.dp)
                    .background(Color.LightGray)
                    .wrapContentHeight(),
                value = keyStorePasswordState.value,
                onValueChange = { keyStorePasswordState.value = it },
            )
            BasicText("alias")
            BasicTextField(
                modifier = Modifier.fillMaxWidth()
                    .height(36.dp)
                    .background(Color.LightGray)
                    .wrapContentHeight(),
                value = aliasState.value,
                onValueChange = { aliasState.value = it },
            )
            BasicText("pin")
            BasicTextField(
                modifier = Modifier.fillMaxWidth()
                    .height(36.dp)
                    .background(Color.LightGray)
                    .wrapContentHeight(),
                value = pinState.value,
                onValueChange = { pinState.value = it },
            )
            BasicText(
                modifier = Modifier.fillMaxWidth()
                    .height(64.dp)
                    .clickable {
                        logics.auth(
                            file = fileState.value,
                            keyStorePassword = keyStorePasswordState.value,
                            alias = aliasState.value,
                            pin = pinState.value,
                        )
                    }
                    .wrapContentSize(),
                text = "auth",
            )
//            val aps = remember { getAuthorizedPackages(context = context, logger = logger, secrets = secrets) }
            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
//                aps.forEachIndexed { index, authorizedPackage ->
//                    item(key = "$index/${authorizedPackage.name}") {
//                        val text = """
//                            pcg: ${authorizedPackage.name}
//                            activity: ${authorizedPackage.activity}
//                            public key: ${secrets.sha256(authorizedPackage.publicKey).toHEX()}
//                        """.trimIndent()
//                        BasicText(
//                            modifier = Modifier.fillMaxWidth()
//                                .background(Color.Yellow)
//                                .clickable {
//                                    logics.enter(
//                                        authorizedPackage = authorizedPackage,
//                                        authority = BuildConfig.PROVIDER_AUTHORITY,
//                                    )
//                                }
//                                .wrapContentHeight(),
//                            text = text,
//                        )
//                    }
//                }
            }
        }
    }
}
