package org.gaziz.birgram.features.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.gaziz.birgram.R
import org.gaziz.birgram.features.chat.ui.component.ChatTopBar
import org.gaziz.birgram.features.chat.ui.component.MessageInputBar
import org.gaziz.birgram.features.chat.ui.model.AvatarUiState
import org.gaziz.birgram.features.chat.ui.model.TitleUiState

@Composable
fun ChatScreen(
    chatId: Long,
    onBack: () -> Unit
) {
    val viewModel = hiltViewModel<ChatViewModel>()
    val chat by viewModel.chat(chatId).collectAsState()
    val messages by viewModel.messages(chatId).collectAsState()
    val containerColor = MaterialTheme.colorScheme.surfaceContainer
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(containerColor),
        topBar = {
            val deletedAccount = stringResource(R.string.deleted_account)
            ChatTopBar(
                avatar = AvatarUiState(
                    avatar = chat?.avatar,
                    size = 40.dp
                ),
                title = TitleUiState(
                    title = if(chat?.isDeleted == true) deletedAccount else chat?.title,
                    fontSize = 6.sp
                ),
                info = chat?.typeInfo,
                onBackClick = onBack,
                onMoreClick = {}
            )
        },
        bottomBar = {
            val height = 60.dp
            MessageInputBar(
                modifier = Modifier.height(height),
                fontSize = 8.sp,
                sendMessage = {}
            )
        },
    ) { paddingValues ->
        if(messages.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(containerColor),
                reverseLayout = true
            ) {
                items(messages.toList()) { (date,_) ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date,
                            fontSize = 5.sp,
                            lineHeight = 5.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            val noMessagesYet = stringResource(R.string.no_messages_yet)
            val fontSize = 6.sp
            Box(
                modifier = Modifier.fillMaxSize().background(containerColor),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                ) {
                    Text(
                        text = noMessagesYet,
                        fontSize = fontSize,
                        lineHeight = fontSize,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.padding(
                            vertical = 4.dp,
                            horizontal = 8.dp
                        )
                    )
                }
            }
        }
    }
}