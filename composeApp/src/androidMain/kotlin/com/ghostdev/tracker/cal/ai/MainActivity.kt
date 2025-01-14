package com.ghostdev.tracker.cal.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ghostdev.tracker.cal.ai.di.initKoin
import com.ghostdev.tracker.cal.ai.utilities.createDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext.stopKoin

class MainActivity : ComponentActivity(), KoinComponent {
    override fun onCreate(savedInstanceState: Bundle?) {
        initKoin {
            androidContext(this@MainActivity)
        }
        createDataStore(applicationContext)

        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }
}