package com.ghostdev.tracker.cal.ai.presentation.home.profile.me.bottomsheets

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghostdev.tracker.cal.ai.presentation.authentication.onboarding.height.TextItem
import kotlinx.coroutines.launch

@Composable
fun HeightBottomSheet(
    heightFeet: Int,
    heightInches: Int,
    onDone: (Int, Int) -> Unit,
    onSheetClosed: () -> Unit,
    isVisible: Boolean
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()
    val meterScrollState = rememberLazyListState(initialFirstVisibleItemIndex = heightFeet - 2)
    val cmScrollState = rememberLazyListState(initialFirstVisibleItemIndex = heightInches - 1)
    val selectedFeet = remember { mutableStateOf(heightFeet) }
    val selectedInches = remember { mutableStateOf(heightInches) }

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
                    text = "Height",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                            }
                        }
                }

                Row(
                    modifier = Modifier.padding(16.dp),
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
                                text = (index + 1).toString(),
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

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    shape = RoundedCornerShape(12.dp),
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                            onDone(selectedFeet.value, selectedInches.value)
                            println(selectedFeet.value)
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
    ) {
        LaunchedEffect(isVisible) {
            if (isVisible) {
                scope.launch { sheetState.show() }
            }
        }
    }
}