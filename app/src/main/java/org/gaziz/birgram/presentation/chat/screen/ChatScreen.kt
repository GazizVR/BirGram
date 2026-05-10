package org.gaziz.birgram.presentation.chat.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.gaziz.birgram.presentation.chat.components.ChatTopBar

@Composable
fun ChatScreen(
    chatId: Long,
    onBack: () -> Unit
){
    Scaffold(
        topBar = {
            ChatTopBar(onBack)
        }
    ) {
        Box(Modifier.padding(it))
    }
}