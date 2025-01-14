package com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.age

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.BaseOnboardingScreen
import com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.pfc.PfcComponent

private const val screenID = 7

class AgeComponent: Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AgeScreen(navigator)
        }
    }
}

@Composable
private fun AgeScreen(
    navigator: Navigator?
) {
    val ageText = remember { mutableStateOf("") }
    val isError = remember { mutableStateOf(false) }

    BaseOnboardingScreen(
        screenID = screenID,
        title = "What’s your age?",
        subtitle = "Required number of calories\nvaries with age",
        onFabClick = { onboardingLogic ->
            if (ageText.value.isEmpty()) {
                isError.value = true
            } else {
                isError.value = false
                onboardingLogic.updateAge(ageText.value)
                navigator?.push(PfcComponent())
            }
        },
        onBackClick = {
            navigator?.pop()
        },
        content = {
            AgeTextComponent(
                ageText = ageText.value,
                onValueChange = { ageText.value = it },
                isError = isError.value
            )
        }
    )
}

@Composable
fun AgeTextComponent(
    ageText: String,
    onValueChange: (String) -> Unit,
    isError: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = ageText,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .width(120.dp)
                .padding(end = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF35CC8C),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Black
            ),
            isError = isError,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true
        )
        Text(
            text = "Years",
            fontSize = 16.sp,
            color = if (isError) Color.Red else Color(0xFF35CC8C)
        )
    }
}