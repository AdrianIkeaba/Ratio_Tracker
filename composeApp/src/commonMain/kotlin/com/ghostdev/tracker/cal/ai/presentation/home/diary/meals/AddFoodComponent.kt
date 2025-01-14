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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cal.composeapp.generated.resources.Res
import cal.composeapp.generated.resources.arrow_back
import com.ghostdev.tracker.cal.ai.models.FoodItem
import com.ghostdev.tracker.cal.ai.presentation.base.BaseLoadingComposable
import com.ghostdev.tracker.cal.ai.presentation.home.components.FoodDialog
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

class AddMealComponent: Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AddMealComponentInit(navigator)
        }
    }
}

@Composable
fun AddMealComponentInit(
    navigator: Navigator?,
    viewmodel: MealLogic = koinViewModel()
) {
    val state by viewmodel.searchMealState.collectAsState()

    state.let { ready ->
        AddMealScreen(
            navigator = navigator,
            food = ready,
            onSearch = {
                viewmodel.searchFood(it)
            },
            addFoodToMeal = { foodItem, servingSize ->
                viewmodel.addFoodToMeal(foodItem, servingSize)
            },
            loading = ready.loading
        )
    }
}


@Composable
private fun AddMealScreen(
    navigator: Navigator?,
    food: SearchFoodUiState?,
    onSearch: (String) -> Unit,
    addFoodToMeal: (FoodItem, String) -> Unit = { _, _ -> },
    loading: Boolean = false
) {
    var showServingDialog by remember { mutableStateOf<FoodItem?>(null) }
    val searchMealText = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
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
                    tint = Color.Gray,
                    contentDescription = "Back"
                )

                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    value = searchMealText.value,
                    singleLine = true,
                    onValueChange = { searchMealText.value = it },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Color.Black
                    ),
                    placeholder = { Text("Describe your meal with sizes", fontSize = 16.sp) },
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onSearch(searchMealText.value)
                        }
                    )
                )
            }

            Spacer(
                modifier = Modifier
                    .height(24.dp)
            )

            if (loading) {
                BaseLoadingComposable()
            } else {
                if (!food?.food?.items.isNullOrEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(food!!.food!!.items) { foodItem ->
                            FoodItem(
                                mealName = foodItem.name,
                                serving = foodItem.serving_size_g.toString(),
                                cal = foodItem.calories.toInt().toString(),
                                onClick = {
                                    showServingDialog = foodItem
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Empty\nType a food name",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    showServingDialog?.let { foodItem ->
        FoodDialog(
            title = foodItem.name,
            calories = foodItem.calories.toInt().toString(),
            proteins = foodItem.protein_g.toInt().toString(),
            fats = foodItem.fat_total_g.toInt().toString(),
            carbs = foodItem.carbohydrates_total_g.toInt().toString(),
            foodServing = foodItem.serving_size_g.toString(),
            unit = "grams",
            onCancel = {
                showServingDialog = null
            },
            onConfirm = { servingSize ->
                addFoodToMeal(foodItem, servingSize)
                showServingDialog = null
                navigator?.pop()
            }
        )
    }
}

@Composable
private fun FoodItem(
    mealName: String,
    serving: String,
    cal: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = mealName.replaceFirstChar { it.uppercase() },
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Row {
                Text(
                    text = "$cal cal",
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )

                Text(
                    modifier = Modifier
                        .padding(start = 2.dp, end = 2.dp),
                    text = "/",
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )

                Text(
                    text = "$serving g",
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            }
        }
    }
}