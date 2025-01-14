package com.ghostdev.tracker.cal.ai.presentation.home.profile.calories

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cal.composeapp.generated.resources.Res
import cal.composeapp.generated.resources.arrow_back
import com.ghostdev.tracker.cal.ai.presentation.home.profile.calories.bottomsheets.CalorieBottomSheet
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel


class CalorieComponent: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CalorieComponentInit(navigator)
        }
    }
}
@Composable
fun CalorieComponentInit(
    navigator: Navigator?,
    viewmodel: CalorieLogic = koinViewModel()
) {

    val state = viewmodel.calorieState.collectAsState()

    LaunchedEffect(Unit) {
        viewmodel.getCalorieData()
    }

    state.let { ready ->
        CalorieScreen(
            navigator = navigator,
            calorie = ready.value.calorie,
            protein = ready.value.protein,
            carbs = ready.value.carbs,
            fat = ready.value.fat,
            water = ready.value.water,
            updateCalorie = {
                viewmodel.updateCalorieData(it)
            },
            updateProtein = {
                viewmodel.updateProteinData(it)
            },
            updateCarbs = {
                viewmodel.updateCarbsData(it)
            },
            updateFat = {
                viewmodel.updateFatData(it)
            },
            updateWater = {
                viewmodel.updateWaterData(it)
            },
            saveUserCaloriesData = {
                viewmodel.saveUserCaloriesData()
            }
        )
    }
}

@Composable
private fun CalorieScreen(
    navigator: Navigator?,
    calorie: String? = null,
    protein: String? = null,
    carbs: String? = null,
    fat: String? = null,
    water: String? = null,
    updateCalorie: (String) -> Unit = {},
    updateProtein: (String) -> Unit = {},
    updateCarbs: (String) -> Unit = {},
    updateFat: (String) -> Unit = {},
    updateWater: (String) -> Unit = {},
    saveUserCaloriesData: () -> Unit = {}
) {
    val label = remember { mutableStateOf("") }
    val showBottomSheet = remember { mutableStateOf(false) }

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
                    text = "Me",
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

            CaloriesDataItem(
                label = "Calories",
                value = "$calorie Cal",
                onClick = {
                    label.value = it
                    showBottomSheet.value = true
                }
            )

            CaloriesDataItem(
                label = "Proteins",
                value = "$protein g",
                onClick = {
                    label.value = it
                    showBottomSheet.value = true
                }
            )

            CaloriesDataItem(
                label = "Fats",
                value = "$fat g",
                onClick = {
                    label.value = it
                    showBottomSheet.value = true
                }
            )

            CaloriesDataItem(
                label = "Carbs",
                value = "$carbs g",
                onClick = {
                    label.value = it
                    showBottomSheet.value = true
                }
            )

            CaloriesDataItem(
                label = "Water",
                value = "$water L",
                onClick = {
                    label.value = it
                    showBottomSheet.value = true
                }
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(22.dp),
            shape = RoundedCornerShape(12.dp),
            onClick = {
                saveUserCaloriesData()
                navigator?.pop()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF35CC8C))
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Save",
                fontSize = 18.sp
            )
        }
    }
    if (label.value == "Calories") {
        CalorieBottomSheet(
            currentValue = calorie ?: "",
            label = label.value,
            secondaryText = "Cal",
            onDone = {
                updateCalorie(it)
                showBottomSheet.value = false
            },
            onSheetClosed = {
                showBottomSheet.value = false
            },
            isVisible = showBottomSheet.value,
        )
    }
    if (label.value == "Proteins") {
        CalorieBottomSheet(
            currentValue = protein ?: "",
            label = label.value,
            secondaryText = "g",
            onDone = {
                updateProtein(it)
                showBottomSheet.value = false
            },
            onSheetClosed = {
                showBottomSheet.value = false
            },
            isVisible = showBottomSheet.value,
        )
    }
    if (label.value == "Fats") {
        CalorieBottomSheet(
            currentValue = fat ?: "",
            label = label.value,
            secondaryText = "g",
            onDone = {
                updateFat(it)
                showBottomSheet.value = false
            },
            onSheetClosed = {
                showBottomSheet.value = false
            },
            isVisible = showBottomSheet.value,
        )
    }
    if (label.value == "Carbs") {
        CalorieBottomSheet(
            currentValue = carbs ?: "",
            label = label.value,
            secondaryText = "g",
            onDone = {
                updateCarbs(it)
                showBottomSheet.value = false
            },
            onSheetClosed = {
                showBottomSheet.value = false
            },
            isVisible = showBottomSheet.value,
        )
    }
    if (label.value == "Water") {
        CalorieBottomSheet(
            currentValue = water ?: "",
            label = label.value,
            secondaryText = "L",

            onDone = {
                updateWater(it)
                showBottomSheet.value = false
            },
            onSheetClosed = {
                showBottomSheet.value = false
            },
            isVisible = showBottomSheet.value,
        )
    }
}



@Composable
private fun CaloriesDataItem(
    label: String = "",
    value: String = "",
    onClick: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = { onClick(label) }
            )
            .height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )

        Spacer(
            modifier = Modifier
                .width(50.dp)
        )

        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF35CC8C)
        )
    }
    HorizontalDivider(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp)
    )
}
















