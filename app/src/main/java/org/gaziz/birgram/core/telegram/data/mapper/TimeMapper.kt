package org.gaziz.birgram.core.telegram.data.mapper

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Int.fromUnixTimeStamp(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime {
    return Instant
        .ofEpochSecond(this.toLong())
        .atZone(zoneId)
        .toLocalDateTime()
}

fun LocalDateTime.formatChatCard(locale: Locale = Locale.getDefault()): String {
    val now = LocalDate.now()
    val date = toLocalDate()

    return when {
        date == now -> format(DateTimeFormatter.ofPattern("HH:mm"))

        date.isAfter(now.minusDays(7)) -> format(DateTimeFormatter.ofPattern("EEE", locale))

        date.year == now.year -> format(DateTimeFormatter.ofPattern("d MMM", locale))

        else -> format(DateTimeFormatter.ofPattern("dd.MM.yy"))
    }
}

fun LocalDateTime.formatMessageCard(): String {
    return format(DateTimeFormatter.ofPattern("HH:mm"))
}

fun LocalDate.formatMessagesList(locale: Locale = Locale.getDefault()): String {
    val now = LocalDate.now()

    return when {
        this.year == now.year -> format(DateTimeFormatter.ofPattern("d MMM", locale))
        else -> format(DateTimeFormatter.ofPattern("d MMM, yyyy", locale))
    }
}