package org.gaziz.birgram.presentation.chat.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.gaziz.birgram.domain.model.chat.ChatType
import org.gaziz.birgram.presentation.chat.components.ChatTopBar
import org.gaziz.birgram.presentation.chat.viewmodel.ChatViewModel

@Composable
fun ChatScreen(
    chatId: Long,
    onBack: () -> Unit
){
    val viewModel = hiltViewModel<ChatViewModel>()
    val chat by viewModel.getChat(chatId).collectAsState()
    val containerColor = CardDefaults.cardColors().containerColor
    Scaffold(
        topBar = {
            if(chat != null)  {
                ChatTopBar(
                    onBack = onBack,
                    photo = chat?.photo,
                    title = chat?.title ?: "",
                    type = chat?.type ?: ChatType.Other
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(containerColor),
        ) {

        }
    }
}