package org.gaziz.birgram.core.telegram.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.gaziz.birgram.core.telegram.ui.model.AvatarUiState

@Composable
fun Avatar(
    modifier: Modifier,
    avatar: AvatarUiState,
    placeHolderFontSize: TextUnit = 16.sp,
    overlay: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        when(avatar) {
            is AvatarUiState.Photo -> {
                AsyncImage(
                    model = avatar.path,
                    contentDescription = null,
                    modifier = modifier.clip(CircleShape),
                )
            }
            is AvatarUiState.Thumbnail -> {
                LaunchedEffect(Unit) {
                    avatar.onDownload()
                }
                Image(
                    bitmap = avatar.bitmap,
                    contentDescription = null,
                    modifier = modifier.clip(CircleShape),
                )
            }
            is AvatarUiState.PlaceHolder -> {
                LaunchedEffect(Unit) {
                    avatar.onDownload()
                }
                Box(
                    modifier = modifier
                        .clip(CircleShape)
                        .background(avatar.color),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = avatar.text,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = placeHolderFontSize,
                        textAlign = TextAlign.Center,
                        lineHeight = placeHolderFontSize
                    )
                }
            }
            is AvatarUiState.Icon -> {
                Box(
                    modifier = modifier
                        .clip(CircleShape)
                        .background(avatar.background),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = avatar.imageVector,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
        overlay()
    }
}