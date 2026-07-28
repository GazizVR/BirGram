package org.gaziz.birgram.features.chat.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import org.gaziz.birgram.R

@Composable
fun MessageInputBar(
    modifier: Modifier = Modifier,
    defaultText: String,
    fontSize: TextUnit,
    sendMessage: (String) -> Unit,
    setDraft: (String) -> Unit
) {
    var message by rememberSaveable { mutableStateOf(defaultText) }
    val containerColor = MaterialTheme.colorScheme.background
    val indicatorColor = Color.Transparent
    val placeholderText = stringResource(R.string.message)
    DisposableEffect(Unit) {
        onDispose {
            setDraft(message)
        }
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor)
    ) {
        Row {
            TextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = placeholderText,
                        fontSize = fontSize,
                        lineHeight = fontSize
                    )
                },
                textStyle = MaterialTheme.typography.labelSmall.copy(
                    fontSize = fontSize,
                    lineHeight = fontSize
                ),
                colors = TextFieldDefaults.colors().copy(
                    unfocusedContainerColor = containerColor,
                    errorContainerColor = containerColor,
                    disabledContainerColor = containerColor,
                    focusedContainerColor = containerColor,

                    focusedIndicatorColor = indicatorColor,
                    errorIndicatorColor = indicatorColor,
                    disabledIndicatorColor = indicatorColor,
                    unfocusedIndicatorColor = indicatorColor
                )
            )
        }
    }
}