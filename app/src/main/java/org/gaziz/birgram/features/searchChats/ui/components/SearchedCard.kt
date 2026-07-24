package org.gaziz.birgram.features.searchChats.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.core.ui.component.ChatAvatar
import org.gaziz.birgram.core.ui.component.ChatText
import org.gaziz.birgram.features.searchChats.ui.model.PhotoUiState
import org.gaziz.birgram.features.searchChats.ui.model.TextUiState
import org.gaziz.birgram.features.searchChats.ui.model.TypeInfoUiState

@Composable
fun SearchedCard(
    modifier: Modifier,
    photo: PhotoUiState,
    title: TextUiState,
    typeInfo: TypeInfoUiState?,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ChatAvatar(
                modifier = Modifier.size(photo.size),
                avatar = photo.avatar,
                placeHolderFontSize = 10.sp,
                overlay = {},
            )
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                ChatText(
                    modifier = Modifier,
                    text = title.text,
                    color = title.color,
                    fontSize = title.fontSize,
                )
                if(typeInfo != null) {
                    Spacer(Modifier.height(8.dp))
                    ChatTypePreview(
                        info = typeInfo.info,
                        fontSize = typeInfo.fontSize
                    )
                }
            }
        }
    }
}