package org.gaziz.birgram.features.chat.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun MessageInputBar(
    modifier: Modifier = Modifier,
    sendMessage: (String) -> Unit
) {
    var message by rememberSaveable { mutableStateOf("") }
    val containerColor = MaterialTheme.colorScheme.background
    val indicatorColor = Color.Transparent
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
                textStyle = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
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