package org.gaziz.birgram.features.searchChats.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import org.gaziz.birgram.R
import org.gaziz.birgram.core.telegram.api.model.user.UserStatus
import org.gaziz.birgram.features.searchChats.domain.model.ChatTypeInfo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun formatLastSeenDate(date: LocalDateTime): String {
    val now = LocalDate.now()

    return when {
        date.year == now.year ->
            date.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm"))

        else ->
            date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }
}

@Composable
fun userStatusString(status: UserStatus): String {
    return when (status) {
        UserStatus.Empty -> stringResource(R.string.status_last_seen_long_ago)
        UserStatus.LastWeek -> stringResource(R.string.status_last_seen_within_week)
        UserStatus.LastMonth -> stringResource(R.string.status_last_seen_within_month)

        is UserStatus.Online -> stringResource(R.string.status_online)
        is UserStatus.Recently -> stringResource(R.string.status_last_seen_recently)
        is UserStatus.Offline -> {
            val now = LocalDateTime.now()
            when {
                status.lastOnline.toLocalDate() == now.toLocalDate() -> {
                    stringResource(R.string.status_last_seen_at) + " " +
                    status.lastOnline.format(DateTimeFormatter.ofPattern("HH:mm"))
                }

                status.lastOnline.toLocalDate() == now.minusDays(1).toLocalDate() -> {
                    stringResource(R.string.status_last_seen_yesterday_at) + " " +
                    status.lastOnline.format(DateTimeFormatter.ofPattern("HH:mm"))
                }

                else -> {
                    stringResource(R.string.status_last_seen_at) + " " +
                    formatLastSeenDate(status.lastOnline)
                }
            }
        }
    }
}
@Composable
fun ChatTypePreview(
    modifier: Modifier = Modifier,
    info: ChatTypeInfo,
    fontSize: TextUnit
) {
    var color = MaterialTheme.colorScheme.onBackground.copy(0.5f)
    val group = stringResource(R.string.group)
    val members = stringResource(R.string.members)
    val subscribers = stringResource(R.string.subscribers)
    val channel = stringResource(R.string.channel)
    val bot = stringResource(R.string.bot)
    val text: String = when(info) {
        is ChatTypeInfo.BasicGroup -> {
            if(info.memberCount > 1) {
                "${info.memberCount} $members"
            } else {
               group
            }
        }

        is ChatTypeInfo.SuperGroup -> {
            if(info.memberCount > 1) {
                "${info.memberCount} ${if (info.isChannel) subscribers else members}"
            } else {
                if(info.isChannel) channel else group
            }
        }

        is ChatTypeInfo.User -> {
            if(info.isBot) {
                bot
            } else {
                if(info.status is UserStatus.Online) {
                    color = MaterialTheme.colorScheme.primary
                }
                userStatusString(info.status)
            }
        }

        else -> ""
    }
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        lineHeight = fontSize,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}