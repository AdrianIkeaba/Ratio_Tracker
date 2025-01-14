package com.ghostdev.tracker.cal.ai.presentation.home.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghostdev.tracker.cal.ai.presentation.home.diary.NutrientsIndicatorState


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NutrientsIndicatorComponent(
    nutrientsData: NutrientsIndicatorState?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color(0xFFF7F7F7)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NutritionItem(
                    label = "Proteins",
                    current = nutrientsData?.currentProteins ?: 0,
                    target = nutrientsData?.recommendedProteins ?: 1,
                    color = Color(0xFFE56571)
                )
                NutritionItem(
                    label = "Fats",
                    current = nutrientsData?.currentFats ?: 0,
                    target = nutrientsData?.recommendedFats ?: 1,
                    color = Color(0xFFFFA072)
                )
                NutritionItem(
                    label = "Carbs",
                    current = nutrientsData?.currentCarbs ?: 0,
                    target = nutrientsData?.recommendedCarbs ?: 1,
                    color = Color(0xFF59FFC4)
                )
            }
            NutritionItem(
                label = "Calories",
                current = nutrientsData?.currentCalories ?: 0,
                target = nutrientsData?.recommendedCalories ?: 1,
                color = Color(0xFF35CC8C),
                fullWidth = true
            )
        }
    }
}

@Composable
fun NutritionItem(
    label: String,
    current: Int,
    target: Int,
    color: Color,
    fullWidth: Boolean = false
) {
    // Ensure we don't divide by zero
    val safeTarget = if (target == 0) 1 else target
    val progress = current.toFloat() / safeTarget.toFloat()

    Column(
        modifier = if (fullWidth) Modifier.fillMaxWidth() else Modifier.wrapContentWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$current / $target",
            fontSize = 14.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        ProgressBar(
            progress = if (progress > 1f) 1f else progress,
            color = color,
            fullWidth = fullWidth
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ProgressBar(
    progress: Float,
    color: Color,
    fullWidth: Boolean = false,
    animationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )
) {
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(progress) {
        animatedProgress.animateTo(
            targetValue = if (progress > 1f) 1f else progress,
            animationSpec = animationSpec
        )
    }

    Box(
        modifier = if (fullWidth) {
            Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(12.dp))
        } else {
            Modifier
                .width(100.dp)
                .height(8.dp)
                .clip(RoundedCornerShape(12.dp))
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress.value)
                .background(color)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray.copy(alpha = 0.3f))
        )
    }
}