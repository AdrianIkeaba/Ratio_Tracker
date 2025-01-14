package com.ghostdev.tracker.cal.ai.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ghostdev.tracker.cal.ai.utilities.formatDate
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarComponent(onDismiss: (String?, LocalDate?) -> Unit) {
    val datePickerState = rememberDatePickerState()

    val dateValidator: (Long?) -> Boolean = { date ->
        val currentDateMillis = Clock.System.now().toEpochMilliseconds()
        date != null && date <= currentDateMillis + 86400000
    }
    val confirmEnabled = remember {
        derivedStateOf {
            datePickerState.selectedDateMillis != null &&
                    dateValidator(datePickerState.selectedDateMillis)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
    ) {
        DatePickerDialog(
            onDismissRequest = { onDismiss(null, null) },
            confirmButton = {
                TextButton(onClick = {
                    val timestampMillis = datePickerState.selectedDateMillis!!
                    val instant = Instant.fromEpochMilliseconds(timestampMillis)
                    val timeZone = TimeZone.currentSystemDefault()
                    val localDateTime = instant.toLocalDateTime(timeZone)
                    val selectedDate = localDateTime.date

                    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                    val yesterday = today.minus(1, DateTimeUnit.DAY)

                    val formattedDate = when (localDateTime.date) {
                        today -> "Today"
                        yesterday -> "Yesterday"
                        else -> formatDate(localDateTime)
                    }

                    onDismiss(formattedDate, selectedDate)
                }, enabled = confirmEnabled.value) {
                    Text(
                        text = "OK",
                        fontSize = 16.sp,
                        color = if (confirmEnabled.value) Color(0xFF35CC8C) else Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss(null, null) }) {
                    Text(
                        text = "Cancel",
                        fontSize = 16.sp,
                        color = Color(0xFF35CC8C),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color.White,
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    headlineContentColor = Color.Black,
                    selectedDayContainerColor = Color(0xFF35CC8C),
                    selectedDayContentColor = Color.White,
                    disabledDayContentColor = Color.Gray,
                    todayContentColor = Color(0xFF35CC8C),
                    todayDateBorderColor = Color(0xFF35CC8C)
                )
            )
        }
    }
}