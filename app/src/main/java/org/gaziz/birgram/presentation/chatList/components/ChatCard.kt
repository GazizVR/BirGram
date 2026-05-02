package org.gaziz.birgram.presentation.chatList.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.valentinilk.shimmer.shimmer
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.R

@Composable
fun ChatCard(
    isLoading: Boolean = false,
    chatIcon: Any?,
    title: String,
    lastMessage: TdApi.MessageContent?,
    lastMessageDate: String,
    unreadCount: Int,
    unreadMention: Int,
    unreadReaction: Boolean,
    isRead: Boolean? = null,
    isOutgoing: Boolean = false
) {
    val containerHeight = 80.dp
    val contentHeight = 55.dp
    val windowInfo = LocalWindowInfo.current
    val placeHolderColor = MaterialTheme.colorScheme.onBackground.copy(0.5f)
    val placeHolderHeight = (contentHeight / 2) - 8.dp
    val cnt = stringArrayResource(R.array.chats_cnt)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(containerHeight)
            .width(windowInfo.containerDpSize.width)
            .clickable {}
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when {
                !isLoading && title.isBlank() -> {
                    Box(
                        modifier =
                            Modifier
                                .size(contentHeight)
                                .clip(CircleShape)
                                .background(Color.Red),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.skull),
                            contentDescription = cnt[2],
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size((contentHeight/2)+5.dp)
                        )
                    }
                }
                chatIcon != null -> {
                    AsyncImage(
                        model = chatIcon,
                        contentDescription = title,
                        modifier = Modifier.size(contentHeight).clip(CircleShape),
                    )
                }
                else -> {
                    Box(
                        modifier = if (title.isNotBlank()) {
                            Modifier
                                .size(contentHeight)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        } else {
                            Modifier
                                .shimmer()
                                .size(contentHeight)
                                .clip(CircleShape)
                                .background(placeHolderColor)
                        },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (title.isNotEmpty()) title[0].toString() else "",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.W600,
                            style = MaterialTheme.typography.labelLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxHeight()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (!isLoading) {
                        Text(
                            text = title.ifBlank { cnt[2] },
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.weight(2f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Start
                        )
                        Row(
                            modifier = Modifier.weight(1.3f),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            if(isRead != null){
                                Icon(
                                    imageVector = if(isRead) ImageVector.vectorResource(R.drawable.done_all) else ImageVector.vectorResource(R.drawable.check),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                            }
                            Text(
                                text = lastMessageDate,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.End
                            )
                        }
                    } else {
                        Box(
                            Modifier
                                .shimmer()
                                .clip(RoundedCornerShape(0.dp))
                                .background(placeHolderColor)
                                .height(placeHolderHeight)
                                .weight(2f)
                        ) {}
                        Spacer(Modifier.weight(1f))
                        Box(
                            Modifier
                                .shimmer()
                                .clip(RoundedCornerShape(0.dp))
                                .background(placeHolderColor)
                                .height(placeHolderHeight)
                                .weight(0.75f)
                        ) {}
                    }
                }
                Spacer(Modifier.height(if (isLoading) 4.dp else 6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (!isLoading) {
                        when(lastMessage){
                            is TdApi.MessageCall -> {
                                val call = if(lastMessage.isVideo) "${cnt[7]} ${cnt[12]}" else cnt[12]
                                val reason = when(lastMessage.discardReason){
                                    is TdApi.CallDiscardReasonDeclined -> "${cnt[13]} $call"
                                    is TdApi.CallDiscardReasonMissed -> "${cnt[14]} $call"
                                    is TdApi.CallDiscardReasonHungUp -> "${cnt[15]} $call"
                                    is TdApi.CallDiscardReasonDisconnected -> "${cnt[16]} $call"
                                    is TdApi.CallDiscardReasonEmpty -> "${cnt[17]} $call"
                                    is TdApi.CallDiscardReasonUpgradeToGroupCall -> "${cnt[18]}: ${(lastMessage.discardReason as TdApi.CallDiscardReasonUpgradeToGroupCall).inviteLink}"
                                    else -> if(isOutgoing) "${cnt[3]} $call" else "${cnt[4]} $call"
                                }
                                MessageText(
                                    text = if(lastMessage.duration > 0) "$reason (${lastMessage.duration} ${cnt[5]})" else reason,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.weight(12f)
                                )
                            }
                            is TdApi.MessageText -> {
                                MessageText(
                                    text = lastMessage.text.text,
                                    modifier = Modifier.weight(12f)
                                )
                            }
                            is TdApi.MessagePhoto -> {
                                MessageThumbnail(
                                    content = lastMessage.photo.minithumbnail?.data,
                                    contentText = cnt[6]
                                )
                            }
                            is TdApi.MessageVideo -> {
                                MessageThumbnail(
                                    content = lastMessage.video.minithumbnail?.data,
                                    contentText = cnt[7]
                                )
                            }
                            is TdApi.MessageAudio -> {
                                MessageText(
                                    text = "🎧${lastMessage.audio.title} - ${lastMessage.audio.performer}",
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.weight(2f)
                                )
                            }
                            is TdApi.MessageVoiceNote -> {
                                Text(
                                    text = "${cnt[8]} ${cnt[9]}",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.labelSmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.weight(2f)
                                )
                            }
                            is TdApi.MessageVideoNote -> {
                                MessageThumbnail(
                                    content = lastMessage.videoNote.minithumbnail?.data,
                                    contentText = "${cnt[7]} ${cnt[9]}"
                                )
                            }
                            is TdApi.MessageDocument -> {
                                Text(
                                    text = lastMessage.document.fileName,
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.labelSmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.weight(2f)
                                )
                            }
                            is TdApi.MessageAnimation -> {
                                MessageThumbnail(
                                    content = lastMessage.animation.minithumbnail?.data,
                                    contentText = "GIF"
                                )
                            }
                            is TdApi.MessageSticker -> {
                                MessageText(
                                    text = "${lastMessage.sticker.emoji} ${cnt[10]}",
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.weight(2f)
                                )
                            }
                            else -> {
                                MessageText(
                                    text = cnt[11],
                                    modifier = Modifier.weight(2f)
                                )
                            }
                        }
                    } else {
                        Box(
                            Modifier
                                .shimmer()
                                .clip(RoundedCornerShape(0.dp))
                                .background(placeHolderColor)
                                .height(placeHolderHeight)
                                .weight(1f)
                        ) {}
                    }
                    Spacer(Modifier.weight(1f))
                    if(unreadReaction && !isLoading) {
                        Box(
                            modifier =
                                Modifier
                                    .clip(CircleShape)
                                    .background(Color.Red)
                                    .size((contentHeight / 2) + 3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.favorite),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(18.dp).offset(y = 1.dp)
                            )
                        }
                        if (unreadCount > 0 || unreadMention > 0) {
                            Spacer(Modifier.width(6.dp))
                        }
                    }
                    if (unreadCount > 0 || unreadMention > 0 || isLoading) {
                        Box(
                            modifier = if(!isLoading){
                                Modifier
                                    .clip(CircleShape)
                                    .background(
                                        if(unreadMention > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(0.11f)
                                    )
                                    .size((contentHeight / 2) + 3.dp)
                            } else {
                                Modifier
                                    .shimmer()
                                    .clip(CircleShape)
                                    .background(placeHolderColor)
                                    .size((contentHeight / 2) + 3.dp)
                            },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if(!isLoading){
                                    if(unreadMention > 0) "@${unreadMention}" else unreadCount.toString()
                                } else "",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
    }
}
