package org.gaziz.birgram.core.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.TextUnit
import org.gaziz.birgram.R
import org.gaziz.birgram.core.telegram.api.model.user.UserStatus
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun LastOnlineText(
    userStatus: UserStatus,
    fontSize: TextUnit
) {
    var textColor =  MaterialTheme.colorScheme.onBackground.copy(0.5f)
    val lastSeenCnt = stringArrayResource(R.array.last_seen_cnt)
    val seenOffline = stringArrayResource(R.array.seen_offline)
    val lastOffline: (LocalDateTime) -> String = {
        val now = LocalDateTime.now()
        val diff = Duration.between(it, now)
        when {
            diff.toMinutes() < 1 ->
                "${lastSeenCnt[1]} ${seenOffline[0]}"

            it.toLocalDate() == LocalDate.now() ->
                "${lastSeenCnt[1]} ${seenOffline[1]} ${it.format(DateTimeFormatter.ofPattern("HH:mm"))}"

            diff.toDays() < 2 ->
                "${lastSeenCnt[1]} ${seenOffline[2]} ${seenOffline[1]} ${it.format(DateTimeFormatter.ofPattern("HH:mm"))}"

            diff.toDays() < 7 ->
                "${lastSeenCnt[1]} ${lastSeenCnt[3]}"

            diff.toDays() < 30 ->
                "${lastSeenCnt[1]} ${lastSeenCnt[4]}"

            else ->
                "${lastSeenCnt[1]} ${lastSeenCnt[5]}"
        }
    }
    val lastSeen = when(userStatus) {
        is UserStatus.Recently -> "${lastSeenCnt[1]} ${lastSeenCnt[2]}"
        UserStatus.LastWeek -> "${lastSeenCnt[1]} ${lastSeenCnt[3]}"
        UserStatus.LastMonth -> "${lastSeenCnt[1]} ${lastSeenCnt[4]}"
        UserStatus.Empty -> "${lastSeenCnt[1]} ${lastSeenCnt[5]}"
        is UserStatus.Online -> {
            val now = LocalDateTime.now()
            if(now >= userStatus.expires){
               lastOffline(now)
            } else {
                textColor = MaterialTheme.colorScheme.primary
                lastSeenCnt[0]
            }
        }
        is UserStatus.Offline -> lastOffline(userStatus.lastOnline)
    }
    Text(
        text = lastSeen,
        color = textColor,
        style = MaterialTheme.typography.labelSmall,
        maxLines = 1,
        fontSize = fontSize
    )
}