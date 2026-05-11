package org.gaziz.birgram.presentation.chat.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import org.gaziz.birgram.R

@Composable
fun MessageInputBar(){
    var message by rememberSaveable { mutableStateOf("") }
    val containerColor = TopAppBarDefaults.topAppBarColors().containerColor
    val indicatorColor = Color.Transparent
    val placeHolder = stringResource(R.string.message_place_holder)
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
        textStyle = MaterialTheme.typography.labelSmall,
        placeholder = {
            Text(
                text = placeHolder,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelSmall,
            )
        },
        trailingIcon = {
            if(message.isNotBlank()){
                IconButton(
                    onClick = {
                         message = ""
                    },
                ) {
                    Icon(
                        ImageVector.vectorResource(R.drawable.send),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    )
}