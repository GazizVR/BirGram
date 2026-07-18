package org.gaziz.birgram.features.chatList.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.launch
import org.gaziz.birgram.R
import org.gaziz.birgram.core.telegram.api.model.chat.ChatType
import org.gaziz.birgram.core.telegram.api.model.user.UserStatus
import org.gaziz.birgram.core.telegram.api.model.user.UserType
import org.gaziz.birgram.features.chatList.ui.components.ChatCard
import org.gaziz.birgram.features.chatList.ui.components.ChatListMenu
import org.gaziz.birgram.features.chatList.ui.components.ChatListTopBar

@Composable
fun ChatListScreen(
    navigateToSearch: () -> Unit,
    navigateToChat: (Long) -> Unit,
    onLogOut: () -> Unit
) {
    BackHandler { }
    val viewModel = hiltViewModel<ChatListViewModel>()
    val chatList by viewModel.mainChatList.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isDark by viewModel.isDark.collectAsState()
    var isLogOut by rememberSaveable { mutableStateOf(false) }
    Box {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ChatListMenu(
                    isDark = isDark,
                    switchIsDark = { viewModel.switchIsDark(it) },
                    onLogOut = { isLogOut = true }
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
                            if(chat.type is ChatType.Private){
                                val user by viewModel.user(chat.type.userId).collectAsState()
                                if(user?.type == UserType.Regular)  {
                                    user?.status is UserStatus.Online
                                } else {
                                    false
                                }
                            } else {
                                false
                            },
                            { viewModel.downloadChatIcon(chat.id,it) },
                            navigateToChat
                        )
                    }
                }
            }
        }
        if(isLogOut) {
            val cnt = stringArrayResource(R.array.log_out_cnt)
            AlertDialog(
                onDismissRequest = { isLogOut = false },
                title = { Text(cnt[0]) },
                text = { Text(cnt[1]) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            isLogOut = false
                            viewModel.logOut {
                                scope.launch {
                                    drawerState.close()
                                    onLogOut()
                                }
                            }
                        }
                    ) {
                        Text(cnt[0], color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { isLogOut = false }) {
                        Text(cnt[2])
                    }
                }
            )
        }
    }
}