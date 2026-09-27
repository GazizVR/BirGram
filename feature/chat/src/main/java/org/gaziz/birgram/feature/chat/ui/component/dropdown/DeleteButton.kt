package org.gaziz.birgram.feature.chat.ui.component.dropdown

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import org.gaziz.birgram.core.telegram.api.model.message.MessageSendingState
import org.gaziz.birgram.core.ui.icon.delete
import org.gaziz.birgram.feature.chat.R
import org.gaziz.birgram.feature.chat.ui.model.MessageUiState

@Composable
fun DeleteButton(
    onClick: () -> Unit,
    fontSize: TextUnit,
    isCancelSending: Boolean
) {
    val text = if(isCancelSending) {
       stringResource(R.string.cancel_sending)
    } else {
        stringResource(R.string.delete)
    }
    DropdownMenuItem(
        text = {
            Text(
                text = text,
                fontSize = fontSize,
                lineHeight = fontSize,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1
            )
        },
        leadingIcon = {
            Icon(
                imageVector = delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        onClick = onClick
    )
}

@Composable
fun DeleteButtonWrapper(
    message: MessageUiState,
    onClick: () -> Unit,
    fontSize: TextUnit
) {
    if(message.canDeleteForSelf || message.canDeleteForAll) {
        DeleteButton(
            onClick = onClick,
            fontSize = fontSize,
            isCancelSending = message.sendingState is MessageSendingState.Pending
        )
    }
}