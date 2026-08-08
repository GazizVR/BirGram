package org.gaziz.birgram.features.chat.ui.mapper

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow

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

fun Long.toByteCount(): String? {
    if (this <= 0) return null

    val units = arrayOf("B", "KB", "MB", "GB", "TB", "PB")
    val digitGroups = (log10(this.toDouble()) / log10(1024.0)).toInt()

    val index = digitGroups.coerceAtMost(units.lastIndex)
    val value = this / 1024.0.pow(index.toDouble())

    return if (index == 0) {
        "$this B"
    } else {
        String.format(Locale.US, "%.1f %s", value, units[index])
    }
}
