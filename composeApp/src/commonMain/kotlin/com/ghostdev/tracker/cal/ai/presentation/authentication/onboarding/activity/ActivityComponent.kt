package com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.activity

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.BaseOnboardingScreen
import com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.height.HeightComponent

const val screenID = 4

class ActivityComponent: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) {
            ActivityScreen(navigator)
        }
    }
}

@Composable
private fun ActivityScreen(
    navigator: Navigator?
) {
    val selectedButton = remember { mutableStateOf(1) }

    BaseOnboardingScreen(
        screenID = screenID,
        title = "How active are you?",
        subtitle = "A sedentary person burns fewer\ncalories than an active person",
        onFabClick = { onboardingLogic ->
            when (selectedButton.value) {
                1 -> {
                    onboardingLogic.updateActivity("Sedentary")
                }
                2 -> {
                    onboardingLogic.updateActivity("Low active")
                }
                3 -> {
                    onboardingLogic.updateActivity("Active")
                }
                4 -> {
                    onboardingLogic.updateActivity("Very active")
                }
            }
            navigator?.push(HeightComponent())
        },
        onBackClick = {
            navigator?.pop()
        },
        content = {
            ActivityButtons(
                selectedButton = selectedButton
            )
        }
    )
}

@Composable
fun ActivityButtons(
    selectedButton: MutableState<Int>
) {
    Column(
        modifier = Modifier
            .wrapContentSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                selectedButton.value = 1
            },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFF35CC8C),
                containerColor = Color.Transparent,
            ),
            border = BorderStroke(1.dp, if (selectedButton.value == 1) Color(0xFF35CC8C) else Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(14.dp),
                text = "Sedentary",
                fontSize = 20.sp,
                color = Color.Black
            )
        }

        Spacer(
            modifier = Modifier
                .height(12.dp)
        )

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                selectedButton.value = 2
            },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFF35CC8C),
                containerColor = Color.Transparent,
            ),
            border = BorderStroke(1.dp, if (selectedButton.value == 2) Color(0xFF35CC8C) else Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(14.dp),
                text = "Low active",
                fontSize = 20.sp,
                color = Color.Black
            )
        }

        Spacer(
            modifier = Modifier
                .height(12.dp)
        )

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                selectedButton.value = 3
            },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFF35CC8C),
                containerColor = Color.Transparent,
            ),
            border = BorderStroke(1.dp, if (selectedButton.value == 3) Color(0xFF35CC8C) else Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(14.dp),
                text = "Active",
                fontSize = 20.sp,
                color = Color.Black
            )
        }

        Spacer(
            modifier = Modifier
                .height(12.dp)
        )

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                selectedButton.value = 4
            },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFF35CC8C),
                containerColor = Color.Transparent,
            ),
            border = BorderStroke(1.dp, if (selectedButton.value == 4) Color(0xFF35CC8C) else Color.White),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(12.dp),
                text = "Very active",
                fontSize = 20.sp,
                color = Color.Black
            )
        }
    }
}
