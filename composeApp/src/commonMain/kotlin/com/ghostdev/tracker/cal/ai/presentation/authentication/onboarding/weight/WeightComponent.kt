package com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.weight

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.BaseOnboardingScreen
import com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.age.AgeComponent

private const val screenID = 6

class WeightComponent: Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) {
            WeightScreen(navigator)
        }
    }
}


@Composable
private fun WeightScreen(navigator: Navigator?) {
    val weightText = remember { mutableStateOf("") }
    val weightUnit = remember { mutableStateOf("kg") }
    val isError = remember { mutableStateOf(false) }

    BaseOnboardingScreen(
        screenID = screenID,
        title = "What’s your weight?",
        subtitle = "The more you weigh, the more calories\nyour body burns",
        onFabClick = { onboardingLogic ->
            if (weightText.value.isEmpty()) {
                isError.value = true
            } else {
                isError.value = false
                onboardingLogic.updateWeight(weightText.value.toInt())
                onboardingLogic.updateWeightUnit(weightUnit.value)
                navigator?.push(AgeComponent())
            }
        },
        onBackClick = { navigator?.pop() },
        content = {
            WeightText(
                weightText = weightText.value,
                weightUnit = weightUnit,
                onValueChange = {
                    weightText.value = it
                },
                onWeightUnitChange = {
                    weightUnit.value = it
                },
                isError = isError.value
            )
        }
    )
}

@Composable
private fun WeightText(
    weightText: String,
    weightUnit: MutableState<String>,
    onValueChange: (String) -> Unit,
    onWeightUnitChange: (String) -> Unit,
    isError: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = weightText,
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
        Row(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(vertical = 12.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(text = weightUnit.value, fontSize = 16.sp, color = Color.Black)
            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Black)
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = Color(0xFFDBFBED)
            ) {
                DropdownMenuItem(
                    text = { Text("kg") },
                    onClick = {
                        weightUnit.value = "kg"
                        onWeightUnitChange("kg")
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("lbs") },
                    onClick = {
                        weightUnit.value = "lbs"
                        onWeightUnitChange("lbs")
                        expanded = false
                    }
                )
            }
        }
    }
}