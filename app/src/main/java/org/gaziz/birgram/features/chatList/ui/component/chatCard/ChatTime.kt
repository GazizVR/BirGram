package org.gaziz.birgram.features.chatList.ui.component.chatCard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.TextUnit
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm")
private val MONTH_DAY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd")
private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy")

fun LocalDateTime.toChatTime(): String {
    val today = LocalDate.now()

    return when {
        // Сегодня -> 14:35
        toLocalDate() == today -> {
            format(TIME_FORMATTER)
        }

        // На этой неделе -> Mon, Tue...
        toLocalDate().isAfter(today.minusDays(7)) -> {
            dayOfWeek.name
                .lowercase()
                .replaceFirstChar(Char::uppercase)
                .take(3)
        }

        // В этом году -> May 05
        year == today.year -> {
            format(MONTH_DAY_FORMATTER)
        }

        // Старше года -> 09.07.25
        else -> {
            format(DATE_FORMATTER)
        }
    }
}

@Composable
fun ChatTime(
    date: LocalDateTime,
    fontSize: TextUnit
) {
    Text(
        text = date.toChatTime(),
        fontSize = fontSize,
        lineHeight = fontSize,
        maxLines = 1
    )
}