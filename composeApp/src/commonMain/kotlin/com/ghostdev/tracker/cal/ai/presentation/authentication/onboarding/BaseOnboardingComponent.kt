package com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cal.composeapp.generated.resources.Res
import cal.composeapp.generated.resources.arrow_right
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BaseOnboardingScreen(
    screenID: Int,
    title: String,
    subtitle: String,
    onFabClick: (BaseOnboardingLogic) -> Unit,
    onBackClick: () -> Unit,
    content: @Composable (BaseOnboardingLogic) -> Unit,
) {
    val onboardingLogic: BaseOnboardingLogic = koinViewModel()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = subtitle,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                content(onboardingLogic)
            }

            Spacer(modifier = Modifier.height(16.dp))

        }

        if (screenID != 1 && screenID != 8) {
            Text(
                modifier = Modifier
                    .clickable { onBackClick() }
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                text = "Back",
                fontSize = 18.sp,
                color = Color.Gray,
            )
        }
        if (screenID != 8) {
            FloatingActionButton(
                onClick = { onFabClick(onboardingLogic) },
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                containerColor = Color(0xFF35CC8C),
                shape = CircleShape
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(Res.drawable.arrow_right),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        } else {
            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(22.dp),
                shape = RoundedCornerShape(12.dp),
                onClick = { onFabClick(onboardingLogic) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF35CC8C))
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Let's go!",
                    fontSize = 18.sp
                )
            }
        }
    }
}