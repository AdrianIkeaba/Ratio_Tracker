package com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.goal

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
import com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.gender.GenderComponent

private const val screenID = 2

class GoalComponent: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) {
            GoalScreen(navigator)
        }
    }
}

@Composable
private fun GoalScreen(
    navigator: Navigator?
) {
    val selectedButton = remember { mutableStateOf(1) }

    BaseOnboardingScreen(
        screenID = screenID,
        title = "What's your goal?",
        subtitle = "We will calculate daily calories\naccording to your goal",
        onFabClick = { onboardingLogic ->
            when (selectedButton.value) {
                1 -> {
                    onboardingLogic.updateGoal("Lose weight")
                }
                2 -> {
                    onboardingLogic.updateGoal("Keep weight")
                }
                3 -> {
                    onboardingLogic.updateGoal("Gain weight")
                }
            }
            navigator?.push(GenderComponent())
        },
        onBackClick = { navigator?.pop() },
        content = {
            GoalButtons(
                selectedButton = selectedButton
            )
        }
    )
}



@Composable
private fun GoalButtons(
    selectedButton: MutableState<Int>,
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
                containerColor = Color.White,
                contentColor = Color(0xFF35CC8C)
            ),
            border = BorderStroke(1.dp, if (selectedButton.value == 1) Color(0xFF35CC8C) else Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(14.dp),
                text = "Lose weight",
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
                containerColor = Color.White,
                contentColor = Color(0xFF35CC8C)
            ),
            border = BorderStroke(1.dp, if (selectedButton.value == 2) Color(0xFF35CC8C) else Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(14.dp),
                text = "Keep weight",
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
                containerColor = Color.White,
                contentColor = Color(0xFF35CC8C)
            ),
            border = BorderStroke(1.dp, if (selectedButton.value == 3) Color(0xFF35CC8C) else Color.White),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(12.dp),
                text = "Gain weight",
                fontSize = 20.sp,
                color = Color.Black
            )
        }
    }
}