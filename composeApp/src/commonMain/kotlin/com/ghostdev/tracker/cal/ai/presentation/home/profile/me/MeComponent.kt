package com.ghostdev.tracker.cal.ai.presentation.home.profile.me

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.ghostdev.tracker.cal.ai.models.User
import com.ghostdev.tracker.cal.ai.presentation.home.profile.ProfileLogic
import com.ghostdev.tracker.cal.ai.presentation.home.profile.me.bottomsheets.ActivityBottomSheet
import com.ghostdev.tracker.cal.ai.presentation.home.profile.me.bottomsheets.AgeBottomSheet
import com.ghostdev.tracker.cal.ai.presentation.home.profile.me.bottomsheets.GenderBottomSheet
import com.ghostdev.tracker.cal.ai.presentation.home.profile.me.bottomsheets.GoalBottomSheet
import com.ghostdev.tracker.cal.ai.presentation.home.profile.me.bottomsheets.HeightBottomSheet
import com.ghostdev.tracker.cal.ai.presentation.home.profile.me.bottomsheets.NameBottomSheet
import com.ghostdev.tracker.cal.ai.presentation.home.profile.me.bottomsheets.WeightBottomSheet
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

class MeComponent: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) {
            MeComponentInit(navigator)
        }
    }
}

@Composable
private fun MeComponentInit(
    navigator: Navigator?,
    viewmodel: ProfileLogic = koinViewModel()
) {
    val state = viewmodel.profileState.collectAsState()
    val scope = rememberCoroutineScope()

    if (!state.value.isLoading) {
        MeScreen(
            navigator = navigator,
            userDetails = state.value.user,
            updateName = {
                viewmodel.updateName(it)
            },
            updateGoal = {
                viewmodel.updateGoal(it)
            },
            updateAge = {
                viewmodel.updateAge(it)
            },
            updateHeight = { feet, inches ->
                viewmodel.updateHeightFeet(feet)
                viewmodel.updateHeightInches(inches)
            },
            updateWeight = { weight, unit ->
                viewmodel.updateWeight(weight.toFloat())
                viewmodel.updateWeightUnit(unit)
            },
            updateGender = {
                viewmodel.updateGender(it)
            },
            updateActivityLevel = {
                viewmodel.updateActivity(it)
            },
            saveUserDetails = {
                scope.launch {
                    viewmodel.saveUserDetails()
                }
            }
        )
    }
}

@Composable
private fun MeScreen(
    navigator: Navigator?,
    userDetails: User?,
    updateName: (String) -> Unit = {},
    updateGoal: (String) -> Unit = {},
    updateAge: (String) -> Unit = {},
    updateHeight: (Int, Int) -> Unit = { _, _ -> },
    updateWeight: (String, String) -> Unit = { _, _ -> },
    updateGender: (String) -> Unit = {},
    updateActivityLevel: (String) -> Unit = {},
    saveUserDetails: () -> Unit = {}
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

            MeItems(
                label = "Name",
                value = userDetails?.name ?: "",
                onClick = {
                    label.value = it
                    showBottomSheet.value = true
                }
            )

            MeItems(
                label = "Goal",
                value = userDetails?.goal ?: "",
                onClick = {
                    label.value = it
                    showBottomSheet.value = true
                }
            )

            MeItems(
                label = "Age",
                value = userDetails?.age.toString(),
                onClick = {
                    label.value = it
                    showBottomSheet.value = true
                }
            )

            MeItems(
                label = "Height",
                value = "${userDetails?.heightFeet}ft ${userDetails?.heightInches}in",
                onClick = {
                    label.value = it
                    showBottomSheet.value = true
                }
            )

            MeItems(
                label = "Weight",
                value = "${userDetails?.weight} ${userDetails?.weightUnit}",
                onClick = {
                    label.value = it
                    showBottomSheet.value = true
                }
            )

            MeItems(
                label = "Gender",
                value = userDetails?.gender ?: "",
                onClick = {
                    label.value = it
                    showBottomSheet.value = true
                }
            )

            MeItems(
                label = "Lifestyle",
                value = userDetails?.activityLevel ?: "",
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
                saveUserDetails()
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
    if (label.value == "Name") {
        NameBottomSheet(
            currentName = userDetails?.name ?: "",
            onDone = {
                updateName(it)
                showBottomSheet.value = false
            },
            onSheetClosed = {
                showBottomSheet.value = false
            },
            isVisible = showBottomSheet.value
        )
    }
    if (label.value == "Goal") {
        GoalBottomSheet(
            currentGoal = userDetails?.goal ?: "",
            onDone = {
                updateGoal(it)
                showBottomSheet.value = false
            },
            onSheetClosed = {
                showBottomSheet.value = false
            },
            isVisible = showBottomSheet.value
        )
    }
    if (label.value == "Age") {
        AgeBottomSheet(
            currentAge = userDetails?.age?.toString() ?: "",
            onDone = {
                updateAge(it)
                showBottomSheet.value = false
            },
            onSheetClosed = {
                showBottomSheet.value = false
            },
            isVisible = showBottomSheet.value
        )
    }
    if (label.value == "Height") {
        HeightBottomSheet(
            heightFeet = userDetails?.heightFeet ?: 0,
            heightInches = userDetails?.heightInches ?: 0,
            onDone = { feet, inches ->
                updateHeight(feet, inches)
                showBottomSheet.value = false
            },
            onSheetClosed = {
                showBottomSheet.value = false
            },
            isVisible = showBottomSheet.value
        )
    }
    if (label.value == "Weight") {
        WeightBottomSheet(
            currentWeight = userDetails?.weight?.toString() ?: "",
            currentWeightUnit = userDetails?.weightUnit ?: "",
            onDone = { weight, unit ->
                showBottomSheet.value = false
                updateWeight(weight, unit)
            },
            onSheetClosed = {
                showBottomSheet.value = false
            },
            isVisible = showBottomSheet.value
        )
    }
    if (label.value == "Gender") {
        GenderBottomSheet(
            currentGender = userDetails?.gender ?: "",
            onDone = {
                showBottomSheet.value = false
                updateGender(it)
            },
            onSheetClosed = {
                showBottomSheet.value = false
            },
            isVisible = showBottomSheet.value
        )
    }
    if (label.value == "Lifestyle") {
        ActivityBottomSheet(
            currentActivity = userDetails?.activityLevel ?: "",
            onDone = {
                showBottomSheet.value = false
                updateActivityLevel(it)
            },
            onSheetClosed = {
                showBottomSheet.value = false
            },
            isVisible = showBottomSheet.value
        )
    }
}

@Composable
private fun MeItems(
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