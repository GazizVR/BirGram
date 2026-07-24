package org.gaziz.birgram.features.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.gaziz.birgram.R
import org.gaziz.birgram.features.chat.ui.component.ChatTopBar
import org.gaziz.birgram.features.chat.ui.model.AvatarUiState
import org.gaziz.birgram.features.chat.ui.model.TitleUiState

@Composable
fun ChatScreen(
    chatId: Long,
    onBack: () -> Unit
) {
    val viewModel = hiltViewModel<ChatViewModel>()
    val chat by viewModel.chat(chatId).collectAsState()
    val containerColor = MaterialTheme.colorScheme.surfaceContainer
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(containerColor),
        topBar = {
            val height = 80.dp
            val deletedAccount = stringResource(R.string.deleted_account)
            ChatTopBar(
                modifier = Modifier.height(height),
                avatar = AvatarUiState(
                    avatar = chat?.avatar,
                    size = 40.dp
                ),
                title = TitleUiState(
                    title = if(chat?.isDeleted == true) deletedAccount else chat?.title,
                    fontSize = 6.sp
                ),
                onBackClick = onBack,
                onMoreClick = {}
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(containerColor)
        ) {

        }
    }
}