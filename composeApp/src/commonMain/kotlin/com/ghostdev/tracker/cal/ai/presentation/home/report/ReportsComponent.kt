package com.ghostdev.tracker.cal.ai.presentation.home.report

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import cal.composeapp.generated.resources.Res
import cal.composeapp.generated.resources.banana
import com.ghostdev.tracker.cal.ai.models.Weights
import com.ghostdev.tracker.cal.ai.presentation.home.profile.ProfileComponent
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.Symbol
import io.github.koalaplot.core.legend.LegendLocation
import io.github.koalaplot.core.line.LinePlot
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.CategoryAxisModel
import io.github.koalaplot.core.xygraph.DefaultPoint
import io.github.koalaplot.core.xygraph.FloatLinearAxisModel
import io.github.koalaplot.core.xygraph.XYGraph
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@Composable
fun ReportsComponentInit(
    navigator: Navigator?,
    viewmodel: ReportsLogic = koinViewModel()
) {
    val state = viewmodel.reportsState.collectAsState()

    state.let { ready ->
        ReportsContent(
            navigator = navigator,
            weights = ready.value.weightsReport,
            weightUnit = ready.value.weightUnit,
            userName = ready.value.name,
            onDeleteWeight = { viewmodel.deleteWeight(it) },
            onAddWeight = { weight -> viewmodel.addWeight(weight) }
        )
    }
}

@Composable
private fun ReportsContent(
    navigator: Navigator?,
    weights: List<Weights> = emptyList(),
    weightUnit: String? = null,
    userName: String? = null,
    onDeleteWeight: (Weights) -> Unit,
    onAddWeight: (Float) -> Unit
) {
    val navBarHeight = 70.dp
    val showDialog = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = navBarHeight)
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
                        .clickable(
                            onClick = {
                                navigator?.parent?.push(ProfileComponent())
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier.size(45.dp),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = Color.Black)
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
                        text = userName ?: "",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Weight",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                IconButton(
                    onClick = { showDialog.value = true }
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add weight",
                        tint = Color(0xFF35CC8C)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Graph(weights = weights)

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Results",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                if (weights.isNotEmpty())
                Text(
                    text = "Swipe to delete entries",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )

            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(
                    items = weights.sortedByDescending { it.date },
                    key = { it.id }
                ) { weight ->
                    WeightItem(
                        weight = weight,
                        weightUnit = weightUnit ?: "",
                        onDelete = { onDeleteWeight(weight) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

            }
        }
    }

    if (showDialog.value) {
        AddWeightDialog(
            currentWeightUnit = weightUnit ?: "",
            onDone = { weight, _ ->
                onAddWeight(weight.toFloat())
                showDialog.value = false
            },
            onDismiss = {
                showDialog.value = false
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun WeightItem(
    weight: Weights,
    weightUnit: String,
    onDelete: () -> Unit
) {
    val isRemoved = remember { mutableStateOf(false) }
    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                onDelete()
                isRemoved.value = true
                true
            } else {
                isRemoved.value = false
                false
            }
        }
    )

    AnimatedVisibility(
        visible = !isRemoved.value,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = 1000),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismiss(
            state = dismissState,
            background = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFEF5350))
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White
                    )
                }
            },
            dismissContent = {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = formatDate(weight.date),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )

                        Text(
                            text = "${weight.weight.toWeightString()} $weightUnit",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    }
                }
            },
            directions = setOf(DismissDirection.EndToStart)
        )
    }
}


@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
private fun Graph(weights: List<Weights>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))
    ) {
        if (weights.isNotEmpty()) {
            val dataPoints = weights.map { weight ->
                DefaultPoint(formatShortDate(weight.date), weight.weight)
            }

            ChartLayout(
                title = { },
                legend = {},
                legendLocation = LegendLocation.NONE,
                modifier = Modifier.padding(8.dp)
            ) {
                XYGraph(
                    xAxisModel = CategoryAxisModel(
                        weights.map { formatShortDate(it.date) }
                    ),
                    yAxisModel = FloatLinearAxisModel(
                        weights.minOf { it.weight - 5f }..weights.maxOf { it.weight + 5f },
                        minimumMajorTickSpacing = 12.dp
                    ),
                    horizontalMajorGridLineStyle = null,
                    horizontalMinorGridLineStyle = null,
                    verticalMajorGridLineStyle = null,
                    verticalMinorGridLineStyle = null,
                ) {
                    LinePlot(
                        data = dataPoints,
                        lineStyle = LineStyle(
                            brush = SolidColor(Color(0xFF35CC8C)),
                            strokeWidth = 2.dp
                        ),
                        symbol = { _ ->
                            Symbol(
                                size = 8.dp,
                                shape = CircleShape,
                                fillBrush = SolidColor(Color(0xFF35CC8C)),
                                modifier = Modifier.hoverable(remember { MutableInteractionSource() })
                            )
                        }
                    )
                }
            }
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("No weight data available")
            }
        }
    }
}


private fun formatDate(date: LocalDate): String {
    val month = date.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
    return "$month ${date.dayOfMonth}, ${date.year}"
}

private fun formatShortDate(date: LocalDate): String {
    val month = date.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
    return "$month ${date.dayOfMonth}"
}

private fun Float.toWeightString(): String {
    return ((this * 10).roundToInt() / 10.0).toString()
}