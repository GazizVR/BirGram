package org.gaziz.birgram.feature.chat.ui.component.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.gaziz.birgram.feature.chat.R

@Composable
fun DeleteMessageDialog(
    deleteMessageIds: LongArray?,
    user: String? = null,
    onValueChange: (LongArray?) -> Unit,
    onDelete: (LongArray, Boolean) -> Unit
) {
    if(deleteMessageIds != null) {
        var forAll by rememberSaveable { mutableStateOf(false) }
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
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onValueChange(null)
                        onDelete(deleteMessageIds,forAll)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1
                    )
                }
            },
            title = {
                Text(
                    text = stringResource(R.string.delete_message),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.delete_message_prompt),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1
                    )
                    if(user != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { forAll = !forAll }
                        ) {
                            Checkbox(
                                checked = forAll,
                                onCheckedChange = { forAll = it }
                            )
                            Spacer(Modifier.width(2.dp))
                            Text(
                                text = stringResource(R.string.also_delete_for_user,user),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.labelSmall,
                                maxLines = 1
                            )
                        }
                    }
                }
            },
        )
    }
}