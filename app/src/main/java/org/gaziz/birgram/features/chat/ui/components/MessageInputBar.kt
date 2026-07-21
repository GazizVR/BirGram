package org.gaziz.birgram.features.chat.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.R
import org.gaziz.birgram.core.ui.icons.send

@Composable
fun MessageInputBar(
    defaultText: String = "",
    onExit: (String) -> Unit,
    onSend: (String) -> Unit
){
    var message by rememberSaveable { mutableStateOf(defaultText) }
    val containerColor = TopAppBarDefaults.topAppBarColors().containerColor
    val indicatorColor = Color.Transparent
    val placeHolder = stringResource(R.string.message_place_holder)
    DisposableEffect(Unit) {
        onDispose {
            onExit(message)
        }
    }
    TextField(
        value = message,
        onValueChange = { message = it },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            unfocusedContainerColor = containerColor,

            focusedIndicatorColor = indicatorColor,
            disabledIndicatorColor = indicatorColor,
            unfocusedIndicatorColor = indicatorColor,
            errorIndicatorColor = indicatorColor
        ),
        textStyle = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
        placeholder = {
            Text(
                text = placeHolder,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 8.sp
            )
        },
        trailingIcon = {
            if(message.isNotBlank()){
                IconButton(
                    onClick = {
                        onSend(message)
                        message = ""
                    },
                ) {
                    Icon(
                        imageVector = send,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    )
}