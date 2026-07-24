package org.gaziz.birgram.features.searchChats.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.gaziz.birgram.core.ui.component.ChatAvatar
import org.gaziz.birgram.core.ui.component.ChatTitle
import org.gaziz.birgram.features.searchChats.ui.model.PhotoUiState
import org.gaziz.birgram.features.searchChats.ui.model.TextUiState

@Composable
fun SearchedCard(
    modifier: Modifier,
    photo: PhotoUiState,
    title: TextUiState,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            ChatAvatar(
                modifier = Modifier.size(photo.size),
                avatar = photo.avatar,
                overlay = {}
            )
            Spacer(Modifier.width(8.dp))
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                ChatTitle(
                    modifier = Modifier.weight(1f),
                    title = title.text,
                    color = title.color,
                    fontSize = title.fontSize,
                )
            }
        }
    }
}