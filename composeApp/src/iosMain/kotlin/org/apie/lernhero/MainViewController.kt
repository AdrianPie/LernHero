package org.apie.lernhero

import androidx.compose.ui.window.ComposeUIViewController
import com.lernhero.di.initializeKoin

fun MainViewController() = ComposeUIViewController(configure = { initializeKoin() }) { App() }