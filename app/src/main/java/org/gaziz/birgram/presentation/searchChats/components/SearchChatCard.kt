package org.gaziz.birgram.presentation.searchChats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.gaziz.birgram.R
import org.gaziz.birgram.domain.model.UserStatus
import org.gaziz.birgram.domain.model.chat.ChatData
import org.gaziz.birgram.domain.model.chat.ChatType
import org.gaziz.birgram.presentation.common.CardPhoto

@Composable
fun SearchChatCard(
    chatData: ChatData,
    downloadPhoto: (Int) -> Unit,
    onClick: (Long) -> Unit
) {
    val containerHeight = 80.dp
    val iconSize = 60.dp
    val membersType = stringArrayResource(R.array.members_type)
    val groupType = stringArrayResource(R.array.group_type)
    val cardColor = CardDefaults.cardColors().containerColor
    Card(
        shape = RoundedCornerShape(0),
        onClick = { onClick(chatData.id) },
        modifier = Modifier.height(containerHeight)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CardPhoto(
                photo = chatData.photo,
                chatTitle = chatData.title,
                iconSize = iconSize,
                downloadPhoto = downloadPhoto,
                isOnline = if(chatData.type is ChatType.Private) chatData.type.userStatus is UserStatus.Online else false,
                cardColor = cardColor
            )
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = chatData.title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                when(chatData.type) {
                    is ChatType.BasicGroup -> {
                        val text = if(chatData.type.memberCont > 0){
                            "${chatData.type.memberCont} ${membersType[0]}"
                        } else {
                           groupType[0]
                        }
                        Text(
                            text = text,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                            maxLines = 1
                        )
                    }
                    is ChatType.SuperGroup -> {
                        val text = if(chatData.type.memberCont > 0){
                            "${chatData.type.memberCont} ${if(chatData.type.isChannel) membersType[1] else membersType[0]}"
                        } else {
                            if(chatData.type.isChannel) groupType[1] else groupType[2]
                        }
                        Text(
                            text = text,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                            maxLines = 1
                        )
                    }
                    is ChatType.Private -> {
                        if(chatData.type.userStatus != null) {
                            LastSeenText(chatData.type.userStatus)
                        }
                    }
                    is ChatType.Secret -> {
                        if(chatData.type.userStatus != null) {
                            LastSeenText(chatData.type.userStatus)
                        }
                    }
                    ChatType.Other -> {}
                }
            }
        }
    }
}