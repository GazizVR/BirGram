package org.gaziz.birgram.features.chat.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.core.ui.component.ChatAvatar
import org.gaziz.birgram.core.ui.component.ChatText
import org.gaziz.birgram.core.ui.icon.arrowBack
import org.gaziz.birgram.core.ui.icon.moreVert
import org.gaziz.birgram.features.chat.ui.model.AvatarUiState
import org.gaziz.birgram.features.chat.ui.model.TitleUiState

@Composable
fun ChatTopBar(
    modifier: Modifier,
    avatar: AvatarUiState,
    title: TitleUiState,
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WindowInsets.statusBars.asPaddingValues()),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = arrowBack,
                    contentDescription = null,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                if(avatar.avatar != null) {
                    ChatAvatar(
                        modifier = Modifier.size(avatar.size),
                        avatar = avatar.avatar,
                        placeHolderFontSize = 10.sp,
                        overlay = {}
                    )
                }
                Spacer(Modifier.width(8.dp))
                if(title.title != null) {
                    ChatText(
                        text = title.title,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = title.fontSize
                    )
                }
            }
            IconButton(
                onClick = onMoreClick
            ) {
                Icon(
                    imageVector = moreVert,
                    contentDescription = null
                )
            }
        }
    }
}
