package org.gaziz.birgram.presentation.chatList.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.R
import org.gaziz.birgram.presentation.Direction
import org.gaziz.birgram.presentation.TGViewModel

@Composable
fun ChatListTopBar(
    viewModel: TGViewModel
){
    val folders by viewModel.folders.collectAsState()
    val unreadCount by viewModel.unreadChatCounts.collectAsState()
    val containerHeight = 80.dp
    Box(
        modifier = Modifier.fillMaxWidth().height(containerHeight).statusBarsPadding().background(MaterialTheme.colorScheme.surface)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {},
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.menu),
                    contentDescription = "menu",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(26.dp)
                )
            }
            if(folders.isNotEmpty()){
                var selected by rememberSaveable { mutableIntStateOf(0) }
                LazyRow(
                    modifier = Modifier.weight(4f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    itemsIndexed(folders){ index,folder ->
                        val icon = when (folder.icon.name) {
                            "All" -> ImageVector.vectorResource(R.drawable.inbox)
                            "Unread" -> ImageVector.vectorResource(R.drawable.mark_email_unread)
                            "Unmuted" -> ImageVector.vectorResource(R.drawable.notifications_active)
                            "Setup" -> ImageVector.vectorResource(R.drawable.settings)
                            "Custom" -> ImageVector.vectorResource(R.drawable.tune)
                            "Bots" -> ImageVector.vectorResource(R.drawable.smart_toy)
                            "Channels" -> ImageVector.vectorResource(R.drawable.campaign)
                            "Groups" -> ImageVector.vectorResource(R.drawable.groups)
                            "Private" -> ImageVector.vectorResource(R.drawable.lock)
                            "Favorite" -> ImageVector.vectorResource(R.drawable.star)
                            "Like" -> ImageVector.vectorResource(R.drawable.thumb_up)
                            "Love" -> ImageVector.vectorResource(R.drawable.favorite)
                            "Party" -> ImageVector.vectorResource(R.drawable.celebration)
                            "Mask" -> ImageVector.vectorResource(R.drawable.masks)
                            "Cat" -> ImageVector.vectorResource(R.drawable.pets)
                            "Crown" -> ImageVector.vectorResource(R.drawable.crown)
                            "Flower" -> ImageVector.vectorResource(R.drawable.local_florist)
                            "Game" -> ImageVector.vectorResource(R.drawable.sports_esports)
                            "Sport" -> ImageVector.vectorResource(R.drawable.sports_soccer)
                            "Study" -> ImageVector.vectorResource(R.drawable.school)
                            "Work" -> ImageVector.vectorResource(R.drawable.work)
                            "Home" -> ImageVector.vectorResource(R.drawable.home)
                            "Travel" -> ImageVector.vectorResource(R.drawable.travel_explore)
                            "Airplane" -> ImageVector.vectorResource(R.drawable.flight)
                            "Trade" -> ImageVector.vectorResource(R.drawable.swap_horizontal_circle)
                            "Money" -> ImageVector.vectorResource(R.drawable.payments)
                            "Note" -> ImageVector.vectorResource(R.drawable.note_stack)
                            "Book" -> ImageVector.vectorResource(R.drawable.menu_book)
                            "Palette" -> ImageVector.vectorResource(R.drawable.palette)
                            "Light" -> ImageVector.vectorResource(R.drawable.lightbulb)
                            else -> ImageVector.vectorResource(R.drawable.folder)
                        }

                        ChatFolderSection(
                            isSelected = selected == index,
                            onSelected = {
                                if(selected != index){
                                    viewModel.loadChats(TdApi.ChatListFolder().apply { this.chatFolderId = folder.id })
                                    if(selected < index) {
                                        viewModel.animationDirection = Direction.Right
                                    } else {
                                        viewModel.animationDirection = Direction.Left
                                    }
                                    if(index == 0){
                                        viewModel.targetChatList = TdApi.ChatListMain()
                                    } else {
                                        Log.d("FUCK","${folder.id}")
                                        viewModel.targetChatList = TdApi.ChatListFolder().apply { this.chatFolderId = folder.id }
                                    }
                                    selected = index
                                }
                            },
                            title = folder.name.text.text,
                            icon = icon,
                            unreadCount = if(index != 0){
                                unreadCount[TdApi.ChatListFolder().apply { chatFolderId = folder.id }] ?: 0
                            } else {
                                unreadCount[TdApi.ChatListMain()] ?: 0
                            }
                        )
                    }
                }
            } else {
                Spacer(Modifier.weight(4f))
            }
            IconButton(
                onClick = {},
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.search),
                    contentDescription = "menu",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}