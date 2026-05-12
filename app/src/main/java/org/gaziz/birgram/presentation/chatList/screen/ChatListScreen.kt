package org.gaziz.birgram.presentation.chatList.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.launch
import org.gaziz.birgram.presentation.chatList.components.ChatCard
import org.gaziz.birgram.presentation.chatList.components.ChatListMenu
import org.gaziz.birgram.presentation.chatList.components.ChatListTopBar
import org.gaziz.birgram.presentation.chatList.viewmodel.ChatListViewModel

@Composable
fun ChatListScreen(
    navigateToSearch: () -> Unit,
    navigateToChat: (Long) -> Unit
) {
    BackHandler { }
    val viewModel = hiltViewModel<ChatListViewModel>()
    val chatList by viewModel.mainChatList.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isDark by viewModel.isDark.collectAsState()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ChatListMenu(
                isDark = isDark,
                switchIsDark = {viewModel.switchIsDark(it)}
            )
        }
    ) {
        Scaffold(
            topBar = {
                ChatListTopBar(
                    navigateToSearch
                ) {
                    scope.launch { drawerState.open() }
                }
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(it),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(chatList) { chat ->
                    ChatCard(
                        chat,
                        { viewModel.downloadChatPhoto(it) },
                        navigateToChat
                    )
                }
            }
        }
    }
}