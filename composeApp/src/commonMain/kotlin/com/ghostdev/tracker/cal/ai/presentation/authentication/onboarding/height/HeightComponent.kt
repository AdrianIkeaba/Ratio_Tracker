package com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.height

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.BaseOnboardingScreen
import com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.weight.WeightComponent

private const val screenID = 5

class HeightComponent: Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) {
            HeightScreen(navigator)
        }
    }
}

@Composable
private fun HeightScreen(
    navigator: Navigator?
) {
    val heightFt = remember { mutableIntStateOf(0) }
    val heightInches = remember { mutableIntStateOf(0) }

    BaseOnboardingScreen(
        screenID = screenID,
        title = "How tall are you?",
        subtitle = "The taller you are, the more calories\nyour body needs",
        onFabClick = { onboardingLogic ->
            onboardingLogic.updateHeightFeet(heightFeet = heightFt.value - 1)
            onboardingLogic.updateHeightInches(heightInches = heightInches.value)
            navigator?.push(WeightComponent())
        },
        onBackClick = {
            navigator?.pop()
        },
        content = {
            HeightPicker(
                onHeightSelected = { ft, inches ->
                    heightFt.intValue = ft
                    heightInches.intValue = inches
                }
            )
        }
    )
}


@Composable
fun HeightPicker(
    modifier: Modifier = Modifier,
    onHeightSelected: (Int, Int) -> Unit
) {
    val meterScrollState = rememberLazyListState()
    val cmScrollState = rememberLazyListState()

    val selectedFeet = remember { mutableStateOf(1) }
    val selectedInches = remember { mutableStateOf(0) }

    LaunchedEffect(meterScrollState) {
        snapshotFlow { meterScrollState.layoutInfo }
            .collect { layoutInfo ->
                val visibleItems = layoutInfo.visibleItemsInfo
                val viewportCenter = layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset / 2
                val centerItem = visibleItems.minByOrNull {
                    kotlin.math.abs((it.offset + it.size / 2) - viewportCenter)
                }
                if (centerItem != null) {
                    selectedFeet.value = centerItem.index + 1
                    onHeightSelected(selectedFeet.value, selectedInches.value)
                }
            }
    }

    LaunchedEffect(cmScrollState) {
        snapshotFlow { cmScrollState.layoutInfo }
            .collect { layoutInfo ->
                val visibleItems = layoutInfo.visibleItemsInfo
                val viewportCenter = layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset / 2
                val centerItem = visibleItems.minByOrNull {
                    kotlin.math.abs((it.offset + it.size / 2) - viewportCenter)
                }
                if (centerItem != null) {
                    selectedInches.value = centerItem.index
                    onHeightSelected(selectedFeet.value, selectedInches.value)
                }
            }
    }

    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        LazyColumn(
            state = meterScrollState,
            modifier = Modifier
                .width(60.dp)
                .height(150.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            flingBehavior = rememberSnapFlingBehavior(lazyListState = meterScrollState)
        ) {
            items(10) { index ->
                TextItem(
                    text = index.toString(),
                    isSelected = index + 1 == selectedFeet.value
                )
            }
        }

        Text(
            text = "ft",
            modifier = Modifier.padding(horizontal = 8.dp),
            fontSize = 18.sp,
            color = Color.Green
        )

        LazyColumn(
            state = cmScrollState,
            modifier = Modifier
                .width(60.dp)
                .height(150.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            flingBehavior = rememberSnapFlingBehavior(lazyListState = cmScrollState)
        ) {
            items(13) { index ->
                TextItem(
                    text = index.toString(),
                    isSelected = index == selectedInches.value
                )
            }
        }

        Text(
            text = "in",
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 18.sp,
            color = Color.Green
        )
    }
}

@Composable
fun TextItem(
    text: String,
    isSelected: Boolean
) {
    val textColor = if (isSelected) Color.Black else Color.Gray
    val underlineColor = if (isSelected) Color(0xFF35CC8C) else Color.Transparent

    Column(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .height(1.dp)
                .width(40.dp)
                .background(underlineColor)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            style = TextStyle(
                fontSize = if (isSelected) 20.sp else 18.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = textColor
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .height(1.dp)
                .width(40.dp)
                .background(underlineColor)
        )
    }
}