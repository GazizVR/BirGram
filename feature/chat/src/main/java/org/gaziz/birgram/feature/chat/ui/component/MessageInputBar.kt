package org.gaziz.birgram.feature.chat.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.core.ui.icon.send
import org.gaziz.birgram.core.ui.theme.BirGramTheme
import org.gaziz.birgram.feature.chat.R

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
    val scrollState = rememberScrollState()
    val window = LocalWindowInfo.current
    val height = window.containerDpSize.height/5
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .heightIn(max = height),
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
            AnimatedVisibility(
                visible = message.isNotBlank(),
                enter = slideInHorizontally(initialOffsetX = { it }),
                exit = slideOutHorizontally(targetOffsetX = { it })
            ) {
                IconButton(
                    onClick = {
                        sendMessage(message)
                        message = ""
                    }
                ) {
                    Icon(
                        imageVector = send,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MessageInputBarPreview() {
    BirGramTheme(darkTheme = true) {
        MessageInputBar(
            modifier = Modifier.height(80.dp),
            defaultText = "",
            fontSize = 8.sp,
            sendMessage = {},
            setDraft = {}
        )
    }
}