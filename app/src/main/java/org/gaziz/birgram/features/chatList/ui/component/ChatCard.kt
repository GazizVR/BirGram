package org.gaziz.birgram.features.chatList.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.gaziz.birgram.core.telegram.api.model.chat.ChatPhoto
import org.gaziz.birgram.features.chatList.ui.component.chatCard.ChatAvatar
import org.gaziz.birgram.features.chatList.ui.component.chatCard.ChatTitle
import org.gaziz.birgram.features.chatList.ui.component.chatCard.ChatUnreadBadge
import org.gaziz.birgram.features.chatList.ui.model.CardPhoto
import org.gaziz.birgram.features.chatList.ui.model.CardText
import org.gaziz.birgram.features.chatList.ui.model.CardUnreadBadge

@Composable
fun ChatCard(
    modifier: Modifier,
    photo: CardPhoto,
    title: CardText,
    lastMessage: @Composable () -> Unit = {},
    unreadBadge: CardUnreadBadge? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainer),
        shape = RoundedCornerShape(0.dp),
        onClick = onClick
    ) {
        Row(
            modifier = modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            if(photo.model is ChatPhoto?) {
                org.gaziz.birgram.features.chatList.ui.component.chatCard.ChatPhoto(
                    modifier = Modifier.size(photo.size),
                    photo = photo.model,
                    onPhotoNull = photo.onNull,
                    placeHolderColor = photo.placeHolderColor,
                    placeHolderText = if (title.text.isNotBlank()) title.text[0].toString() else "",
                )
            } else {
                ChatAvatar(
                    modifier = Modifier.size(photo.size),
                    imageModel = photo.model,
                    placeHolderColor = photo.placeHolderColor,
                    placeHolderText = if (title.text.isNotBlank()) title.text[0].toString() else "",
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
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    lastMessage()
                    if(unreadBadge != null) {
                        Spacer(Modifier.weight(1f))
                        ChatUnreadBadge(unreadBadge)
                    }
                }
            }
        }
    }
}