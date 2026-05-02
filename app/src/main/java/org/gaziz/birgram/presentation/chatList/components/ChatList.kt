package org.gaziz.birgram.presentation.chatList.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.R
import java.time.Clock
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant

@OptIn(ExperimentalTime::class)
@Composable
fun ChatList(
    chatList: Map<Long,TdApi.Chat>,
    paddingValues: PaddingValues,
    chatsPhotos: Map<Long,Any?>,
    isNewAccount: Boolean
){
    val cnt = stringArrayResource(R.array.chats_cnt)
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(top = paddingValues.calculateTopPadding()),
        verticalArrangement = if(chatList.isNotEmpty()) Arrangement.spacedBy(2.dp) else Arrangement.Center
    ){
        if(chatList.isNotEmpty()){
            items(
                chatList.toList().sortedByDescending { srt -> srt.second.positions.find { it.list is TdApi.ChatListMain }?.order }
            ){ (order,chat) ->
                val rawLastMessageDate = Instant.fromEpochSeconds(chat.lastMessage?.date?.toLong() ?: 0)
                val dateJava = LocalDateTime.ofInstant(
                    rawLastMessageDate.toJavaInstant(),
                    Clock.systemDefaultZone().zone
                )
                val nowDate = LocalDateTime.now()
                val today = nowDate.dayOfYear == dateJava.dayOfYear && nowDate.year == dateJava.year
                val thisWeek = nowDate.minusWeeks(1) < dateJava
                val thisYear = nowDate.year == dateJava.year
                val lastMessageDate = when {
                    today -> {
                        dateJava.format(DateTimeFormatter.ofPattern("HH:mm"))
                    }
                    !today && thisWeek -> {
                        dateJava.dayOfWeek.getDisplayName(TextStyle.SHORT, LocalLocale.current.platformLocale)
                    }
                    thisYear -> {
                        "${dateJava.month.getDisplayName(TextStyle.SHORT, LocalLocale.current.platformLocale)} ${dateJava.dayOfMonth}"
                    }
                    else -> {
                        dateJava.format(DateTimeFormatter.ofPattern("dd.MM.yy"))
                    }
                }
                ChatCard(
                    chatIcon = chatsPhotos[order],
                    title = chat.title,
                    lastMessage = chat.lastMessage?.content,
                    lastMessageDate = lastMessageDate,
                    unreadCount = chat.unreadCount,
                    unreadMention = chat.unreadMentionCount,
                    unreadReaction = chat.unreadReactionCount > 0,
                    isOutgoing = chat.lastMessage?.isOutgoing ?: true,
                    isRead = if(chat.lastMessage != null){
                        if(
                            chat.lastMessage!!.isOutgoing &&
                            !chat.lastMessage!!.isChannelPost
                        ){
                            chat.lastMessage!!.id <= chat.lastReadOutboxMessageId
                        } else {
                            null
                        }
                    } else null
                )
            }
        } else {
            if(isNewAccount){
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = cnt[0],
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = cnt[1],
                            color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(horizontal = 4.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                repeat(10){
                    item {
                        ChatCard(
                            isLoading = true,
                            chatIcon = null,
                            title = "",
                            lastMessage = null,
                            lastMessageDate = "",
                            unreadCount = 0,
                            unreadMention = 0,
                            unreadReaction = false
                        )
                    }
                }
            }
        }
    }
}