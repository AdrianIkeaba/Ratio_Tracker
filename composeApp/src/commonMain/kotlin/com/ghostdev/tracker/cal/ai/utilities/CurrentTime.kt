package com.ghostdev.tracker.cal.ai.utilities

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun getCurrentTimeFormatted(): String {
    val currentTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())

    return LocalTime(currentTime.hour, currentTime.minute).toString()
}

fun getCurrentDate(): LocalDate {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
}

fun getCurrentTime(): LocalTime {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
}