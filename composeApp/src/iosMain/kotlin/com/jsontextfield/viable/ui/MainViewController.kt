package com.jsontextfield.viable.ui

import androidx.compose.ui.window.ComposeUIViewController
import com.jsontextfield.viable.di.initKoin
import com.jsontextfield.viable.ui.App

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }
