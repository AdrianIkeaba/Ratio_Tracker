package com.ghostdev.tracker.cal.ai

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.ghostdev.tracker.cal.ai.presentation.authentication.login.LoginComponent
import com.ghostdev.tracker.cal.ai.presentation.authentication.login.LoginLogic
import com.ghostdev.tracker.cal.ai.presentation.authentication.login.LoginState
import com.ghostdev.tracker.cal.ai.presentation.base.BaseLoadingComposable
import com.ghostdev.tracker.cal.ai.presentation.home.BaseHomeComponent
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    val colorScheme = lightColorScheme(
        primary = Color(0xFF6200EE),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFBB86FC),
        onPrimaryContainer = Color(0xFF3700B3),
        secondary = Color(0xFF03DAC6),
        onSecondary = Color(0xFF000000),
        secondaryContainer = Color(0xFF018786),
        onSecondaryContainer = Color(0xFF000000),
        background = Color(0xFFFFFFFF),
        onBackground = Color(0xFF000000),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF000000),
        error = Color(0xFFCF6679),
        onError = Color(0xFFFFFFFF),
        outline = Color(0xFFBDBDBD),
    )

    val loginLogic: LoginLogic = koinViewModel()
    val loginState by loginLogic.loginState.collectAsState(false)

    MaterialTheme(colorScheme = colorScheme) {
        when (loginState) {
            LoginState.Loading -> {
                BaseLoadingComposable()
            }

            is LoginState.Authenticated -> {
                if ((loginState as LoginState.Authenticated).exists) {
                    Navigator(BaseHomeComponent()) { navigator ->
                        SlideTransition(navigator)
                    }
                } else {
                    Navigator(LoginComponent()) { navigator ->
                        SlideTransition(navigator)
                    }
                }
            }
        }
    }
}