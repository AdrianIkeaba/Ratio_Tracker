package com.ghostdev.tracker.cal.ai

import androidx.compose.ui.window.ComposeUIViewController
import com.ghostdev.tracker.cal.ai.di.initKoin
import com.ghostdev.tracker.cal.ai.utilities.createDataStore

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    createDataStore()
    App()
}