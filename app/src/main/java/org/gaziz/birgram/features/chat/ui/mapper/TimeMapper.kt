package org.gaziz.birgram.features.chat.ui.mapper

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDate.formatMonthDay(locale: Locale = Locale.getDefault()): String {
    val currentYear = Year.now().value

    val formatter = if (year == currentYear) {
        DateTimeFormatter.ofPattern("MMMM d", locale)
    } else {
        DateTimeFormatter.ofPattern("MMMM d, yyyy", locale)
    }

    return format(formatter)
}

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

fun LocalDateTime.toTimeString(): String = format(timeFormatter)