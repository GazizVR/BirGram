package org.gaziz.birgram.feature.chat.ui.component.dropDownContent

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import org.gaziz.birgram.core.ui.icon.delete
import org.gaziz.birgram.feature.chat.R

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
                maxLines = 1,
                color = MaterialTheme.colorScheme.onBackground
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