package org.gaziz.birgram.feature.chat.ui.component.dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.gaziz.birgram.feature.chat.R

@Composable
fun DeleteMessageDialog(
    deleteMessageIds: LongArray?,
    onValueChange: (LongArray?) -> Unit,
    onDelete: (LongArray) -> Unit
) {
    if(deleteMessageIds != null) {
        AlertDialog(
            modifier = Modifier.padding(16.dp),
            onDismissRequest = { onValueChange(null) },
            dismissButton = {
                TextButton(
                    onClick = { onValueChange(null) }
                ) {
                    Text(
                        text = stringResource(android.R.string.cancel),
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onValueChange(null)
                        onDelete(deleteMessageIds)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = MaterialTheme.colorScheme.error,
                        maxLines = 1,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            },
            title = {
                Text(
                    text = stringResource(R.string.delete_message),
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelMedium
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.delete_message_prompt),
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelSmall
                )
            },
        )
    }
}