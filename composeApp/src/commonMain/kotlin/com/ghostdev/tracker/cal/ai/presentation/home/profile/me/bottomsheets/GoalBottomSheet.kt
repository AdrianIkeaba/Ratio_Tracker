package com.ghostdev.tracker.cal.ai.presentation.home.profile.me.bottomsheets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun GoalBottomSheet(
    currentGoal: String,
    onDone: (String) -> Unit,
    onSheetClosed: () -> Unit,
    isVisible: Boolean
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()
    val selectedGoal = remember { mutableStateOf(currentGoal) }

    LaunchedEffect(sheetState.isVisible) {
        if (!sheetState.isVisible) {
            onSheetClosed()
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(2.dp))
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Goal",
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            selectedGoal.value = "Lose weight"
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF35CC8C)
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (selectedGoal.value == "Lose weight") Color(0xFF35CC8C) else Color.LightGray
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(14.dp),
                            text = "Lose weight",
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .height(12.dp)
                    )

                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            selectedGoal.value = "Keep weight"
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF35CC8C)
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (selectedGoal.value == "Keep weight") Color(0xFF35CC8C) else Color.LightGray
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(14.dp),
                            text = "Keep weight",
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .height(12.dp)
                    )

                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            selectedGoal.value = "Gain weight"
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF35CC8C)
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (selectedGoal.value == "Gain weight") Color(0xFF35CC8C) else Color.LightGray
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(12.dp),
                            text = "Gain weight",
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        shape = RoundedCornerShape(12.dp),
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                                onDone(selectedGoal.value)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF35CC8C))
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "Done",
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    ) {
        LaunchedEffect(isVisible) {
            if (isVisible) {
                scope.launch { sheetState.show() }
            }
        }
    }
}