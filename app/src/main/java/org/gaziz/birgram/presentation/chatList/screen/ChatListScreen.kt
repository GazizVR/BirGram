package org.gaziz.birgram.presentation.chatList.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.presentation.Direction
import org.gaziz.birgram.presentation.TGViewModel
import org.gaziz.birgram.presentation.chatList.components.ChatList

@Composable
fun ChatListScreen(
    viewModel: TGViewModel,
    paddingValues: PaddingValues
){
    val chatList by viewModel.chats.collectAsState()
    val chatsPhotos by viewModel.chatsPhotos.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadChats()
    }

    LaunchedEffect(chatList) {
        viewModel.archiveChats.putAll(chatList.filter { it.value.positions.find { it.list is TdApi.ChatListArchive } != null })
    }

    LaunchedEffect(chatList) {
        viewModel.folderChats.clear()
        val chats = chatList.filter { chat ->
            chat.value.positions.find {
                it.list is TdApi.ChatListFolder
            } != null
        }
        chats.forEach { chat ->
            chat.value.positions.forEach {
                if(it.list is TdApi.ChatListFolder){
                    val idi = (it.list as TdApi.ChatListFolder).chatFolderId
                    if(viewModel.folderChats.containsKey(idi)){
                        val map = viewModel.folderChats[idi]!!.toMutableMap().apply { put(chat.key,chat.value) }
                        viewModel.folderChats[idi] = map
                    } else {
                        viewModel.folderChats[idi] = mapOf(chat.key to chat.value)
                    }
                }
            }
        }
    }

    AnimatedContent(
        targetState = viewModel.targetChatList,
        transitionSpec =
            {
                when(viewModel.animationDirection){
                    Direction.Left -> slideInHorizontally(tween(300,50),{-it}).togetherWith(slideOutHorizontally(tween(300),{it}))
                    Direction.Right -> slideInHorizontally(tween(300,50),{it}).togetherWith(slideOutHorizontally(tween(300),{-it}))
                    Direction.Up -> slideInVertically(tween(300,50),{it}).togetherWith(slideOutVertically(tween(300),{-it}))
                    Direction.Down -> slideInVertically(tween(300,50),{-it}).togetherWith(slideOutVertically(tween(300),{it}))
                }
            }
    ) { state ->
        when(state){
            is TdApi.ChatListMain -> {
                ChatList(
                    chatList = chatList,
                    paddingValues = paddingValues,
                    chatsPhotos = chatsPhotos,
                    isNewAccount = viewModel.isNewAccount
                )
            }
            is TdApi.ChatListFolder -> {
                if(viewModel.folderChats.containsKey(state.chatFolderId)){
                    ChatList(
                        chatList = viewModel.folderChats[state.chatFolderId]!!,
                        paddingValues = paddingValues,
                        chatsPhotos = chatsPhotos,
                        isNewAccount = viewModel.isNewAccount
                    )
                }
            }
            is TdApi.ChatListArchive -> {
                ChatList(
                    chatList = viewModel.archiveChats,
                    paddingValues = paddingValues,
                    chatsPhotos = chatsPhotos,
                    isNewAccount = viewModel.isNewAccount
                )
            }
        }
    }
}