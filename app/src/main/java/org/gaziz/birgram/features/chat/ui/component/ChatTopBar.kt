package org.gaziz.birgram.features.chat.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.core.ui.component.ChatAvatar
import org.gaziz.birgram.core.ui.component.ChatText
import org.gaziz.birgram.core.ui.component.ChatTypePreview
import org.gaziz.birgram.core.ui.icon.arrowBack
import org.gaziz.birgram.core.ui.icon.moreVert
import org.gaziz.birgram.core.ui.model.ChatTypeInfo
import org.gaziz.birgram.features.chat.ui.model.AvatarUiState
import org.gaziz.birgram.features.chat.ui.model.TitleUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    modifier: Modifier = Modifier,
    avatar: AvatarUiState,
    title: TitleUiState,
    info: ChatTypeInfo?,
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = arrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
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
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        ChatText(
                            text = title.title,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = title.fontSize
                        )
                        if(info != null) {
                            Spacer(Modifier.height(6.dp))
                            ChatTypePreview(
                                info = info,
                                fontSize = 6.sp
                            )
                        }
                    }
                }
            }
        },
        actions = {
            IconButton(
                onClick = onMoreClick
            ) {
                Icon(
                    imageVector = moreVert,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    )
}
