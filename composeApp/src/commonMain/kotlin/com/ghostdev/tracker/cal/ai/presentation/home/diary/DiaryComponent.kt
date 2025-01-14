package com.ghostdev.tracker.cal.ai.presentation.home.diary

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.Navigator
import cal.composeapp.generated.resources.Res
import cal.composeapp.generated.resources.add
import cal.composeapp.generated.resources.banana
import cal.composeapp.generated.resources.calendar
import com.ghostdev.tracker.cal.ai.models.Meal
import com.ghostdev.tracker.cal.ai.presentation.home.components.CalendarComponent
import com.ghostdev.tracker.cal.ai.presentation.home.components.MealsItem
import com.ghostdev.tracker.cal.ai.presentation.home.components.NutrientsIndicatorComponent
import com.ghostdev.tracker.cal.ai.presentation.home.components.WaterIntakeComponent
import com.ghostdev.tracker.cal.ai.presentation.home.diary.meals.CreateMealComponent
import com.ghostdev.tracker.cal.ai.presentation.home.profile.ProfileComponent
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DiaryComponentInit(
    viewmodel: DiaryLogic = koinViewModel(),
    navigator: Navigator?,
    selectedDate: LocalDate = viewmodel.homeState.value.selectedDate
) {
    val state by viewmodel.homeState.collectAsStateWithLifecycle()
    val currentDate by viewmodel.currentDate.collectAsStateWithLifecycle()

    LaunchedEffect(selectedDate) {
        viewmodel.fetchDataForDate(selectedDate)
    }

    state.nutrientsIndicator?.let { nutrientsData ->
        DiaryComponent(
            navigator = navigator,
            userName = state.userName ?: "",
            nutrientsData = nutrientsData,
            waterIntakeData = state.waterIntake,
            mealsData = state.meals,
            selectedDate = state.selectedDate,
            currentDate = currentDate,
            onDateChange = { newDate ->
                viewmodel.fetchDataForDate(newDate)
            },
            onDateTextChanged = { newDate ->
                viewmodel.updateCurrentDate(newDate)
            },
            onWaterValueChange = { value, date ->
                viewmodel.updateWaterIntake(value, date)
            },
            onMealClick = {
                navigator?.parent?.push(CreateMealComponent(
                    date = selectedDate,
                    mealToEdit = it)
                )
            }
        )
    }
}

@Composable
private fun DiaryComponent(
    navigator: Navigator?,
    userName: String = "",
    nutrientsData: NutrientsIndicatorState,
    waterIntakeData: WaterIntakeState? = null,
    mealsData: MealsState? = null,
    selectedDate: LocalDate,
    currentDate: String,
    onDateChange: (LocalDate) -> Unit = {},
    onDateTextChanged: (String) -> Unit = {},
    onWaterValueChange: (Float, LocalDate) -> Unit = { _, _ -> },
    onMealClick: (Meal) -> Unit = {},
) {
    val navBarHeight = 85.dp
    var calendarDialogShow by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clickable {
                                navigator?.parent?.push(ProfileComponent())
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            modifier = Modifier.size(45.dp),
                            shape = CircleShape,
                            backgroundColor = Color.Black
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

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = userName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    Row(
                        modifier = Modifier.clickable { calendarDialogShow = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currentDate,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(Res.drawable.calendar),
                            contentDescription = "Calendar Icon",
                            tint = Color(0xFF5CB85C),
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "Nutrients Indicator",
                    fontSize = 16.sp
                )
                NutrientsIndicatorComponent(nutrientsData)

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "Water Intake",
                    fontSize = 16.sp
                )
                WaterIntakeComponent(
                    waterIntakeData = waterIntakeData,
                    selectedDate = selectedDate,
                    onWaterIntakeChange = { value, date ->
                        onWaterValueChange(value, date)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = "Meals",
                        fontSize = 16.sp
                    )
                    Icon(
                        modifier = Modifier
                            .clickable { navigator?.parent?.push(CreateMealComponent(date = selectedDate)) }
                            .alignByBaseline()
                            .size(24.dp),
                        painter = painterResource(Res.drawable.add),
                        contentDescription = null
                    )
                }
            }

            if (!mealsData?.meals.isNullOrEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .safeDrawingPadding()
                        .padding(bottom = navBarHeight, start = 16.dp, end = 16.dp)
                ) {
                    mealsData?.meals?.forEachIndexed { index, meal ->
                        item {
                            MealsItem(
                                mealType = meal.name ?: "Meal $index",
                                calories = meal.totalCalories.toString(),
                                time = meal.time ?: "Unknown",
                                meal = mealsData.meals[index].meal,
                                onMealClick = { onMealClick(mealsData.meals[index].meal) }
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        text = "No meals for Today",
                        fontSize = 18.sp
                    )
                }
            }
        }
    }

    if (calendarDialogShow) {
        CalendarComponent(onDismiss = { formattedDate, newDate ->
            calendarDialogShow = false
            formattedDate?.let {
                onDateChange(newDate!!)
                onDateTextChanged(formattedDate)
            }
        })
    }
}