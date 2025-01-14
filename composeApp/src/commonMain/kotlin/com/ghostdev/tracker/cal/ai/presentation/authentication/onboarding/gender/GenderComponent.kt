package com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.gender

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
import com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.activity.ActivityComponent

const val screenID = 3

class GenderComponent: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) {
            GenderScreen(navigator)
        }
    }
}

@Composable
private fun GenderScreen(
    navigator: Navigator?
) {
    val selectedButton = remember { mutableStateOf(1) }

    BaseOnboardingScreen(
        screenID = screenID,
        title = "What's your gender?",
        subtitle = "Male bodies need more calories",
        onFabClick = { onboardingLogic ->
            when (selectedButton.value) {
                1 -> {
                    onboardingLogic.updateGender("Male")
                }
                2 -> {
                    onboardingLogic.updateGender("Female")
                }
            }
            navigator?.push(ActivityComponent())
        },
        onBackClick = {
            navigator?.pop()
        },
        content = {
            GenderButtons(
                selectedButton = selectedButton
            )
        }
    )
}

@Composable
private fun GenderButtons(
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
                containerColor = Color.Transparent,
                contentColor = Color(0xFF35CC8C)
            ),
            border = BorderStroke(1.dp, if (selectedButton.value == 1) Color(0xFF35CC8C) else Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(14.dp),
                text = "Male",
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
                containerColor = Color.Transparent,
                contentColor = Color(0xFF35CC8C)
            ),
            border = BorderStroke(1.dp, if (selectedButton.value == 2) Color(0xFF35CC8C) else Color.White),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(12.dp),
                text = "Female",
                fontSize = 20.sp,
                color = Color.Black
            )
        }
    }
}