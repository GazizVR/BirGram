package org.gaziz.birgram.feature.chat.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.core.ui.component.ChatAvatar
import org.gaziz.birgram.feature.chat.ui.component.dropDownContent.CopyButtonWrapper
import org.gaziz.birgram.feature.chat.ui.component.dropDownContent.DeleteButton
import org.gaziz.birgram.feature.chat.ui.component.messageContent.ContentPreview
import org.gaziz.birgram.feature.chat.ui.model.MessageUiState

@Composable
fun MessageCard(
    message: MessageUiState,
    fontSize: TextUnit,
    onDropdownOpen: () -> Unit,
    onDelete: () -> Unit
) {
    val containerColor = if(message.isOutgoing){
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerLow
    }
    val spacerSize = 40.dp
    var expanded by remember { mutableStateOf(false) }
    var hasMenuOpened by rememberSaveable { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                if(!hasMenuOpened) {
                    onDropdownOpen()
                    hasMenuOpened = true
                }
                expanded = true
            },
        contentAlignment = if(message.isOutgoing) Alignment.CenterEnd else Alignment.CenterStart,
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if(
                message.sender != null &&
                !message.isOutgoing
            ) {
                if(message.sender.avatar != null) {
                    ChatAvatar(
                        modifier = Modifier.size(spacerSize),
                        avatar = message.sender.avatar!!,
                        placeHolderFontSize = 10.sp,
                    )
                } else {
                    Spacer(Modifier.width(spacerSize))
                }
            } else {
                if(message.isOutgoing) {
                    Spacer(Modifier.width(spacerSize))
                }
            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = if(message.isOutgoing) Alignment.CenterEnd else Alignment.CenterStart
            ) {
                ContentPreview(
                    msgId = message.id,
                    content = message.content,
                    fontSize = fontSize,
                    dateStr = message.date,
                    containerColor = containerColor,
                    senderInfo = if(!message.isOutgoing) message.sender else null,
                    sendingState = message.sendingState,
                    originSenderTitle = message.originSenderTitle,
                    isOutgoing = message.isOutgoing
                )
            }
            if(!message.isOutgoing) {
                Spacer(Modifier.width(spacerSize))
            }
        }
        Box(
            contentAlignment = Alignment.Center
        ) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(8.dp),
            ) {
                CopyButtonWrapper(
                    msgCnt = message.content,
                    fontSize = fontSize,
                    onClick = { expanded = false }
                )
                if(message.canDeleteForSelf || message.canDeleteForAll) {
                    DeleteButton(
                        onClick = {
                            expanded = false
                            onDelete()
                        },
                        fontSize = fontSize
                    )
                }
            }
        }
    }
}