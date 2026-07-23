package org.gaziz.birgram.features.chatList.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.gaziz.birgram.core.telegram.api.model.chat.ChatPhoto
import org.gaziz.birgram.features.chatList.ui.component.chatCard.CardPhoto
import org.gaziz.birgram.features.chatList.ui.component.chatCard.ChatAvatar
import org.gaziz.birgram.features.chatList.ui.component.chatCard.ChatTime
import org.gaziz.birgram.features.chatList.ui.component.chatCard.ChatTitle
import org.gaziz.birgram.features.chatList.ui.component.chatCard.ChatUnreadBadge
import org.gaziz.birgram.features.chatList.ui.model.CardTextUiState
import org.gaziz.birgram.features.chatList.ui.model.LastMsgUiState
import org.gaziz.birgram.features.chatList.ui.model.PhotoUiState
import org.gaziz.birgram.features.chatList.ui.model.UnreadBadgeUiState

@Composable
fun ChatCard(
    modifier: Modifier,
    photo: PhotoUiState,
    title: CardTextUiState,
    lastMessage: LastMsgUiState? = null,
    unreadBadge: UnreadBadgeUiState? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.clickable { onClick() },
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            if(photo.model is ChatPhoto?) {
                CardPhoto(
                    modifier = Modifier.size(photo.size),
                    photo = photo.model,
                    onPhotoNull = photo.onNull,
                    placeHolderColor = photo.placeHolderColor,
                    placeHolderText = if (title.text.isNotBlank()) title.text[0].toString() else "",
                    overlay = photo.overlay
                )
            } else {
                ChatAvatar(
                    modifier = Modifier.size(photo.size),
                    imageModel = photo.model,
                    placeHolderColor = photo.placeHolderColor,
                    placeHolderText = if (title.text.isNotBlank()) title.text[0].toString() else "",
                    overlay = photo.overlay
                )
            }
            Spacer(Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    ChatTitle(
                        title = title.text,
                        color = title.color,
                        fontSize = title.fontSize,
                        modifier = Modifier.weight(1f)
                    )
                    if(lastMessage != null) {
                        ChatTime(
                            date = lastMessage.date,
                            fontSize = lastMessage.fontSize
                        )
                    }
                }
                Spacer(Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if(lastMessage != null) {
                        lastMessage.component(Modifier.weight(1f))
                    }
                    if(unreadBadge != null) {
                        if(lastMessage == null) {
                            Spacer(Modifier.weight(1f))
                        }
                        ChatUnreadBadge(unreadBadge)
                    }
                }
            }
        }
    }
}