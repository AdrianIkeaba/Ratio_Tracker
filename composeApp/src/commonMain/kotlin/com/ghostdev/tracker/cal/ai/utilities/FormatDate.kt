package com.ghostdev.tracker.cal.ai.utilities

import kotlinx.datetime.LocalDateTime

fun formatDate(localDateTime: LocalDateTime): String {
    val dayOfWeek =
        localDateTime.dayOfWeek.name.take(3) // E.g., "Wed"
    val month = localDateTime.month.name.take(3) // E.g., "Jan"
    val dayOfMonth = localDateTime.dayOfMonth // E.g., 8
    return "$dayOfWeek, $month $dayOfMonth"
}