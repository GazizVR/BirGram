package org.gaziz.birgram.feature.chat.ui.component.dropDownContent

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.gaziz.birgram.core.telegram.api.model.message.MessageSendingState
import org.gaziz.birgram.core.ui.icon.refresh
import org.gaziz.birgram.feature.chat.R

@Composable
fun RetryButton(
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Text(
                text = stringResource(R.string.retry),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1
            )
        },
        leadingIcon = {
            Icon(
                imageVector = refresh,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        onClick = onClick
    )
}

@Composable
fun RetryButtonWrapper(
    sendingState: MessageSendingState?,
    onRetry: () -> Unit
) {
    if(
        sendingState is MessageSendingState.Failed &&
        sendingState.canRetry
    ) {
        RetryButton(onClick = onRetry)
    }
}