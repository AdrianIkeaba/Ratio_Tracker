package com.ghostdev.tracker.cal.ai.presentation.home.diary.meals

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.internal.BackHandler
import com.ghostdev.tracker.cal.ai.models.Meal
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.compose.viewmodel.koinViewModel

class CreateMealComponent(
    private val date: LocalDate,
    private val mealToEdit: Meal? = null
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CreateMealComponentInit(
                navigator = navigator,
                date = date,
                mealToEdit = mealToEdit
            )
        }
    }
}


@OptIn(InternalVoyagerApi::class)
@Composable
fun CreateMealComponentInit(
    navigator: Navigator?,
    viewmodel: MealLogic = koinViewModel(),
    date: LocalDate,
    mealToEdit: Meal? = null
) {
    val state by viewmodel.createMealState.collectAsState()
    val scope = rememberCoroutineScope()

    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(mealToEdit) {
        isLoading.value = true
        mealToEdit?.let { meal ->
            viewmodel.loadMealForEditing(meal)
        }
        isLoading.value = false
    }

    BackHandler(
        true,
        onBack = {
            viewmodel.clearMealState()
            navigator?.pop()
        }
    )

    CreateMealScreen(
        navigator = navigator,
        state = state,
        isEditing = mealToEdit != null,
        saveMeal = {
            scope.launch {
                if (mealToEdit != null) {
                    viewmodel.updateMeal(it, date, mealToEdit.id)
                } else {
                    viewmodel.saveMeal(it, date)
                }
            }
        },
        onDelete = {
            viewmodel.removeFoodFromMeal(it)
        },
        onMealNameChange = {
            viewmodel.updateMealName(it)
        }
    )
}



@Composable
private fun CreateMealScreen(
    navigator: Navigator?,
    state: CreateMealState,
    isEditing: Boolean = false,
    saveMeal: (String) -> Unit = {},
    onDelete: (Int) -> Unit = {},
    onMealNameChange: (String) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
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
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Black
                )

                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    value = state.mealName,
                    onValueChange = { onMealNameChange(it) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Color.Black
                    ),
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                )

                Icon(
                    modifier = Modifier
                        .size(25.dp)
                        .clickable(
                            onClick = {
                                navigator?.push(AddMealComponent())
                            }
                        ),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Nutrition summary
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp)
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NutritionValue(
                    value = state.totalCalories.toInt().toString(),
                    label = "Calories"
                )

                NutritionValue(
                    value = "${state.totalProteins.toInt()}g",
                    label = "Proteins"
                )

                NutritionValue(
                    value = "${state.totalFats.toInt()}g",
                    label = "Fats"
                )

                NutritionValue(
                    value = "${state.totalCarbs.toInt()}g",
                    label = "Carbs"
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            )

            // Food list
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(state.foods) { food ->
                    AddedMealItem(
                        food = food,
                        onDelete = {
                            onDelete(state.foods.indexOf(food))
                        }
                    )
                }
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(12.dp),
            onClick = {
                saveMeal(state.mealName)
                navigator?.pop()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF35CC8C)
            )
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = if (isEditing) "Update" else "Save",
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun NutritionValue(
    value: String,
    label: String
) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
            ) {
                append("$value\n")
            }
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                )
            ) {
                append(label)
            }
        },
        fontSize = 18.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun AddedMealItem(
    food: FoodUiState,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = food.name,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Row {
                    Text(
                        text = "${food.servingSize}g",
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )

                    Text(
                        modifier = Modifier
                            .padding(start = 4.dp, end = 4.dp),
                        text = "•",
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )

                    Text(
                        text = "${food.calories.toInt()} cal",
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                }
            }

            Icon(
                modifier = Modifier
                    .size(25.dp)
                    .clickable(onClick = onDelete),
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.Gray
            )
        }
    }
}