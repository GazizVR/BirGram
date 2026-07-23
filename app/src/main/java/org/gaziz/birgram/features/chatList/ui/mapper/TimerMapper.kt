package org.gaziz.birgram.features.chatList.ui.mapper

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm")
private val MONTH_DAY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd")
private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy")

fun LocalDateTime.toStringDate(): String {
    val today = LocalDate.now()

    return when {
        toLocalDate() == today -> {
            format(TIME_FORMATTER)
        }

        toLocalDate().isAfter(today.minusDays(7)) -> {
            dayOfWeek.name
                .lowercase()
                .replaceFirstChar(Char::uppercase)
                .take(3)
        }

        year == today.year -> {
            format(MONTH_DAY_FORMATTER)
        }

        else -> {
            format(DATE_FORMATTER)
        }
    }
}
