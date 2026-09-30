package org.gaziz.birgram.feature.chat.ui.component.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import org.gaziz.birgram.feature.chat.ui.model.MessageUiState

@Composable
fun MessageActionMenu(
    msg: MessageUiState,
    onDismissRequest: () -> Unit,
    onDelete: (Long) -> Unit,
    onRetry: (Long) -> Unit
) {
    Popup(
        alignment = Alignment.Center,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(focusable = true),
        content = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
            ) {
                Column {
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
            }
        }
    )
}