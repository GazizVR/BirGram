package org.gaziz.birgram.feature.chat.ui.component.menu

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Popup
import org.gaziz.birgram.feature.chat.ui.model.MessageUiState

@Composable
fun MessageActionMenu(
    selectedMsgId: Long?,
    getMessage: (Long) -> MessageUiState?,
    onDismissRequest: () -> Unit,
    onDelete: (Long) -> Unit,
    onRetry: (Long) -> Unit
) {
    if(selectedMsgId == null) return
    val msg = getMessage(selectedMsgId)
    if (msg != null) {
        Popup(
            alignment = Alignment.Center,
            onDismissRequest = onDismissRequest,
            content = {
                RetryButtonWrapper(
                    sendingState = msg.sendingState,
                    onRetry = { onRetry(msg.id) }
                )
                CopyButtonWrapper(
                    msgCnt = msg.content,
                    onClick = onDismissRequest
                )
                DeleteButtonWrapper(
                    message = msg,
                    onClick = {
                        onDismissRequest()
                        onDelete(msg.id)
                    }
                )
            }
        )
    }
}