package com.ghostdev.tracker.cal.ai.presentation.home.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cal.composeapp.generated.resources.Res
import cal.composeapp.generated.resources.arrow_back
import cal.composeapp.generated.resources.arrow_right
import cal.composeapp.generated.resources.banana
import com.ghostdev.tracker.cal.ai.presentation.home.profile.calories.CalorieComponent
import com.ghostdev.tracker.cal.ai.presentation.home.profile.me.MeComponent
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

class ProfileComponent: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ProfileComponentInit(navigator)
        }
    }
}

@Composable
private fun ProfileComponentInit(
    navigator: Navigator?,
    viewmodel: ProfileLogic = koinViewModel()
) {
    val state = viewmodel.profileState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewmodel.getUserData()
    }


    if (!state.value.isLoading) {
        state.let { ready ->
            ProfileScreen(
                navigator = navigator,
                userName = ready.value.user?.name ?: "",
                recommendedCalories = ready.value.user?.recommendedCalories.toString(),
                weightUnit = ready.value.user?.weightUnit ?: "",
            )
        }
    }
}

@Composable
private fun ProfileScreen(
    navigator: Navigator?,
    userName: String = "",
    recommendedCalories: String = "0 Cal",
    weightUnit: String = "",
    waterServingSize: String = "200 mL"
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .safeDrawingPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp)
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                navigator?.pop()
                            }
                        )
                        .size(25.dp),
                    painter = painterResource(Res.drawable.arrow_back),
                    contentDescription = "Back",
                    tint = Color.Black
                )

                Text(
                    text = "Profile",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier
                        .width(25.dp)
                )
            }

            Spacer(
                modifier = Modifier
                .height(40.dp)
            )

            Card(
                modifier = Modifier
                    .size(120.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black
                )
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    painter = painterResource(Res.drawable.banana),
                    contentDescription = "Profile Picture",
                    tint = Color.Unspecified
                )
            }

            Spacer(
                modifier = Modifier
                    .height(12.dp)
            )

            Text(
                text = userName,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier
                    .height(24.dp)
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                onClick = {
                    navigator?.push(MeComponent())
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF35CC8C)
                ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .padding(10.dp),
                        text = "Me",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Icon(
                        modifier = Modifier
                            .size(18.dp),
                        painter = painterResource(Res.drawable.arrow_right),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .height(12.dp)
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                onClick = {
                    navigator?.push(CalorieComponent())
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDBFBED)
                ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .padding(10.dp),
                        text = "Calorie Intake",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )

                    Text(
                        modifier = Modifier
                            .padding(10.dp),
                        text = "$recommendedCalories Cal",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF35CC8C)
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .height(12.dp)
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDBFBED)
                ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .padding(10.dp),
                        text = "Weight Unit",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )

                    Text(
                        modifier = Modifier
                            .padding(10.dp),
                        text = weightUnit,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF35CC8C)
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .height(12.dp)
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDBFBED)
                ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .padding(10.dp),
                        text = "Water Serving Size",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )

                    Text(
                        modifier = Modifier
                            .padding(10.dp),
                        text = waterServingSize,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF35CC8C)
                    )
                }
            }

        }
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            text = "KotlinConf2025",
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}