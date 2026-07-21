package org.gaziz.birgram.features.chatList.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.gaziz.birgram.R
import org.gaziz.birgram.core.telegram.api.model.chat.ChatType
import org.gaziz.birgram.core.telegram.api.model.user.UserType
import org.gaziz.birgram.core.ui.icons.arrowBack
import org.gaziz.birgram.core.ui.icons.moreVert
import org.gaziz.birgram.core.ui.icons.skull
import org.gaziz.birgram.features.chatList.ui.components.ChatCard

@Composable
fun ArchiveScreenTopBar(
    title: String,
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(WindowInsets.statusBars.asPaddingValues()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onBackClick
        ) {
            Icon(
                imageVector = arrowBack,
                contentDescription = null
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall
        )
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

@Composable
fun ArchiveScreen(
    onBack: () -> Unit,
    onChatClick: (Long) -> Unit
) {
    val archivedChats = stringResource(R.string.archived_chats)
    val window = LocalWindowInfo.current
    val cardHeight = 70.dp
    val cardWidth = window.containerDpSize.width

    val viewModel = hiltViewModel<ChatListViewModel>()
    val chats by viewModel.archiveChatList.collectAsState()
    val users by viewModel.users.collectAsState()
    Scaffold(
        topBar = {
            ArchiveScreenTopBar(
                title = archivedChats,
                onBackClick = onBack,
                onMoreClick = {}
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(chats) { chat ->
                val accentColor by viewModel.getAccentColor(chat.accentColorId).collectAsState()
                val isDeleted =
                    users[(chat.type as? ChatType.Private)?.userId]?.type is UserType.Deleted ||
                    users[(chat.type as? ChatType.Private)?.userId]?.type is UserType.Unknown
                ChatCard(
                    modifier = Modifier
                        .height(cardHeight)
                        .width(cardWidth),
                    photoModel = if(isDeleted) skull else chat.photo,
                    onPhotoNull = { fileId -> viewModel.downloadChatIcon(chat.id,fileId) },
                    photoSize = 54.dp,
                    photoPlaceHolderColor = accentColor,
                    title = if(isDeleted) stringResource(R.string.deleted_account) else chat.title,
                    titleFontSize = 7.sp,
                    onClick = { onChatClick(chat.id) }
                )
            }
        }
    }
}