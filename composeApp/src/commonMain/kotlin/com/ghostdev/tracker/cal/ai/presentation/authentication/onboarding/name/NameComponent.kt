package com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.name

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.BaseOnboardingScreen
import com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.goal.GoalComponent

private const val screenID = 1

class NameComponent: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) {
            NameScreen(navigator)
        }
    }
}


@Composable
private fun NameScreen(
    navigator: Navigator?
) {
    val nameText = remember { mutableStateOf("") }
    val isError = remember { mutableStateOf(false) }

    BaseOnboardingScreen(
        screenID = screenID,
        title = "What’s your name?",
        subtitle = "We can't just call you 'Ratio',\n we need a name :)",
        onFabClick = { onboardingLogic ->
            if (nameText.value.isEmpty()) {
                isError.value = true
            } else {
                isError.value = false
                onboardingLogic.updateName(nameText.value)
                navigator?.push(GoalComponent())
            }
        },
        onBackClick = {
            navigator?.pop()
        },
        content = {
            NameTextField(
                nameText = nameText.value,
                onValueChange = {
                    nameText.value = it
                },
                isError = isError.value
            )
        }
    )
}

@Composable
fun NameTextField(
    nameText: String,
    onValueChange: (String) -> Unit = {},
    isError: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        OutlinedTextField(
            value = nameText,
            placeholder = { if (isError) Text("Please enter a name", color = Color.Red) },
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .width(200.dp)
                .align(Alignment.Center)
                .padding(end = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF35CC8C),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Black
            ),
            isError = isError,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true
        )
    }
}