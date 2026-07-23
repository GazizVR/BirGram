package org.gaziz.birgram.features.chatList.ui.component.chatCard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import org.gaziz.birgram.features.chatList.domain.model.ChatAvatar

@Composable
fun ChatAvatar(
    modifier: Modifier,
    avatar: ChatAvatar,
    overlay: @Composable () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        when(avatar) {
            is ChatAvatar.Photo -> {
                Image(
                    bitmap = avatar.bitmap,
                    contentDescription = null,
                    modifier = modifier.clip(CircleShape),
                )
            }
            is ChatAvatar.Icon -> {
                Box(
                    modifier = modifier
                        .clip(CircleShape)
                        .background(avatar.background),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = avatar.imageVector,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            is ChatAvatar.PlaceHolder -> {
                LaunchedEffect(Unit) {
                    avatar.callback()
                }
                Box(
                    modifier = modifier
                        .clip(CircleShape)
                        .background(avatar.color),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = avatar.text,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
        overlay()
    }
}