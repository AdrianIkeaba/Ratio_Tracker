package com.ghostdev.tracker.cal.ai.presentation.authentication.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cal.composeapp.generated.resources.Res
import cal.composeapp.generated.resources.ratio_logo
import com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.name.NameComponent
import org.jetbrains.compose.resources.painterResource

class LoginComponent : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) {
            WelcomeScreen(navigator)
        }
    }
}

@Composable
private fun WelcomeScreen(
    navigator: Navigator?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier
                .height(50.dp)
        )

        Image(
            modifier = Modifier
                .size(250.dp),
            painter = painterResource(Res.drawable.ratio_logo),
            contentDescription = null
        )

        WelcomeText()

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            shape = RoundedCornerShape(12.dp),
            onClick = {
                navigator?.push(NameComponent())
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF35CC8C)
            )
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "Start",
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun WelcomeText() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = "Welcome!",
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Congratulations on taking the first step toward a healthier you!",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.Light
        )
    }
}

