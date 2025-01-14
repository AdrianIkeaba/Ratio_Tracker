package com.ghostdev.tracker.cal.ai.presentation.home.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cal.composeapp.generated.resources.Res
import cal.composeapp.generated.resources.add
import cal.composeapp.generated.resources.remove
import com.ghostdev.tracker.cal.ai.presentation.home.diary.WaterIntakeState
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.painterResource

@Composable
fun WaterIntakeComponent(
    waterIntakeData: WaterIntakeState?,
    selectedDate: LocalDate,
    onWaterIntakeChange: (Float, LocalDate) -> Unit = { _, _ -> }
) {
    val currentIntake = waterIntakeData?.currentWaterIntake
    val recommendedIntake = waterIntakeData?.recommendedWaterIntake
    val percentage = waterIntakeData?.currentWaterIntakePercentage
    val lastUpdated = waterIntakeData?.lastUpdatedTime

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color(0xFFF7F7F7)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Water",
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        ) {
                            append("${currentIntake ?: 0.0} / ")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        ) {
                            append("${recommendedIntake}L")
                        }
                    },
                    color = Color.Black
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Last time: ${lastUpdated ?: "Never"}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Column(
                modifier = Modifier.padding(start = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(50))
                        .clip(RoundedCornerShape(50)),
                    onClick = {
                        onWaterIntakeChange(0.2f, selectedDate)
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(Res.drawable.add),
                        contentDescription = null,
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(50))
                        .clip(RoundedCornerShape(50)),
                    onClick = {
                        onWaterIntakeChange(-0.2f, selectedDate)
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(Res.drawable.remove),
                        contentDescription = null,
                    )
                }
            }

            Column(
                modifier = Modifier.padding(start = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedWaterProgressBar(percentage = (percentage ?: 0).toFloat())
            }
        }
    }
}

@Composable
fun AnimatedWaterProgressBar(
    percentage: Float,
    animationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )
) {
    val animatedPercentage = remember { Animatable(0f) }

    LaunchedEffect(percentage) {
        animatedPercentage.animateTo(
            targetValue = if (percentage > 100f) 100f else percentage,
            animationSpec = animationSpec
        )
    }

    Box(
        modifier = Modifier
            .width(50.dp)
            .height(150.dp)
            .clip(CircleShape)
            .background(Color.LightGray.copy(alpha = 0.3f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight((animatedPercentage.value) / 100f)
                .align(Alignment.BottomStart)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF81DDFF),
                            Color(0xFF78ABFF)
                        )
                    )
                )
        )

        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            text = if (animatedPercentage.value > 100) "100%" else "${animatedPercentage.value.toInt()}%",
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}