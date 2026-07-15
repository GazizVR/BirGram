package org.gaziz.birgram.core.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.TextUnit
import org.gaziz.birgram.R
import org.gaziz.birgram.features.chatList.domain.model.chat.ChatType

@Composable
fun ChatTypeCnt(
    type: ChatType,
    fontSize: TextUnit? = null
) {
    val membersType = stringArrayResource(R.array.members_type)
    val groupType = stringArrayResource(R.array.group_type)
    when(type) {
        is ChatType.BasicGroup -> {
            val text = if(type.memberCont > 0){
                "${type.memberCont} ${membersType[0]}"
            } else {
                groupType[0]
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                maxLines = 1,
                fontSize = fontSize ?: MaterialTheme.typography.labelSmall.fontSize
            )
        }
        is ChatType.SuperGroup -> {
            val text = if(type.memberCont > 0){
                "${type.memberCont} ${if(type.isChannel) membersType[1] else membersType[0]}"
            } else {
                if(type.isChannel) groupType[1] else groupType[2]
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                maxLines = 1,
                fontSize = fontSize ?: MaterialTheme.typography.labelSmall.fontSize
            )
        }
        is ChatType.Private -> {
            if(type.userStatus != null) {
                LastOnlineText(type.userStatus,fontSize)
            }
        }
        is ChatType.Secret -> {
            if(type.userStatus != null) {
                LastOnlineText(type.userStatus,fontSize)
            }
        }
        ChatType.Other -> {}
    }
}