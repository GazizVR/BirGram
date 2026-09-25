package org.gaziz.birgram.feature.chat.ui.component.dropDownContent

import android.content.ClipData
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import kotlinx.coroutines.launch
import org.gaziz.birgram.core.ui.icon.contentCopy
import org.gaziz.birgram.feature.chat.R
import org.gaziz.birgram.feature.chat.ui.model.MessageContentInfo

@Composable
fun CopyButton(
    text: String,
    fontSize: TextUnit,
    onClick: () -> Unit,
) {
    val clipboardManager = LocalClipboard.current
    val scope = rememberCoroutineScope()
    DropdownMenuItem(
        text = {
            Text(
                text = stringResource(R.string.copy),
                fontSize = fontSize,
                lineHeight = fontSize,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        leadingIcon = {
            Icon(
                imageVector = contentCopy,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
            )
        },
        onClick = {
            scope.launch {
                val data = ClipData.newPlainText("", text)
                val clip = ClipEntry(data)
                clipboardManager.setClipEntry(clip)
            }
            onClick()
        }
    )
}

@Composable
fun CopyButtonWrapper(
    msgCnt: MessageContentInfo,
    fontSize: TextUnit,
    onClick: () -> Unit,
) {
    when(msgCnt) {
        is MessageContentInfo.Text -> CopyButton(msgCnt.text,fontSize,onClick)
        is MessageContentInfo.Animation -> {
            val text = msgCnt.caption
            if(text != null) {
                CopyButton(text,fontSize,onClick)
            }
        }
        is MessageContentInfo.Photo -> {
            val text = msgCnt.caption
            if(text != null) {
                CopyButton(text,fontSize,onClick)
            }
        }
        is MessageContentInfo.Video -> {
            val text = msgCnt.caption
            if(text != null) {
                CopyButton(text,fontSize,onClick)
            }
        }
        else -> {}
    }
}