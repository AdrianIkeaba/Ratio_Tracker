package com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.pfc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.BaseOnboardingScreen
import com.ghostdev.tracker.cal.ai.presentation.home.BaseHomeComponent

private const val screenID = 8

class PfcComponent: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) {
            PfcScreen(navigator)
        }
    }
}

@Composable
private fun PfcScreen(
    navigator: Navigator?
) {
    val triggerSave = remember { mutableStateOf(false) }

    BaseOnboardingScreen(
        screenID = screenID,
        title = "Recommended PFC",
        subtitle = "You can always change PFC in\nthe profile",
        onFabClick = {
            triggerSave.value = true
        },
        onBackClick = {
            navigator?.pop()
        },
        content = { onboardingLogic ->
            val onboardingState = onboardingLogic.onboardingState.collectAsState()
            val pfcMap = onboardingLogic.calculatePFC(onboardingState.value)

            LaunchedEffect(triggerSave.value) {
                if (triggerSave.value) {
                    onboardingLogic.setUserExists()
                    onboardingLogic.saveUserDetails()
                    navigator?.replace(BaseHomeComponent())
                }
            }

            PfcContent(
                proteins = pfcMap?.get("protein") ?: 0,
                fats = pfcMap?.get("fat") ?: 0,
                carbs = pfcMap?.get("carbs") ?: 0,
                calories = pfcMap?.get("calories") ?: 0
            )
        }
    )
}

@Composable
private fun PfcContent(
    proteins: Int = 0,
    fats: Int = 0,
    carbs: Int = 0,
    calories: Int = 0
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF35CC8C))
                .padding(16.dp)
        ) {
            Text(
                text = "Proteins: ${proteins}g",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF35CC8C))
                .padding(16.dp)
        ) {
            Text(
                text = "Fats: ${fats}g",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF35CC8C))
                .padding(16.dp)
        ) {
            Text(
                text = "Carbs: ${carbs}g",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF35CC8C))
                .padding(16.dp)
        ) {
            Text(
                text = "Calories: $calories Cal",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}