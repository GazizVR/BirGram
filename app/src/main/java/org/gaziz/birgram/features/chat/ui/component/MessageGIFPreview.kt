package org.gaziz.birgram.features.chat.ui.component

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import org.gaziz.birgram.features.chat.ui.model.MediaContent
import org.gaziz.birgram.features.chat.ui.model.MessageContentInfo

@Composable
fun MessageGIFPreview(
    content: MessageContentInfo.GIF,
    containerColor: Color,
    date: String,
    fontSize: TextUnit
) {
    val shape = RoundedCornerShape(18.dp)
    Box(
        modifier = Modifier
            .width(content.width.dp)
            .height(content.height.dp)
            .clip(shape)
            .border(1.dp,containerColor,shape)
            .background(containerColor)
    ) {
        when(content.content) {
            is MediaContent.Media -> {
                val context = LocalContext.current
                val imageLoader = remember(context) {
                    ImageLoader.Builder(context)
                        .components {
                            if (SDK_INT >= 28) {
                                add(AnimatedImageDecoder.Factory())
                            } else {
                                add(GifDecoder.Factory())
                            }                        }
                        .build()
                }
                AsyncImage(
                    model = content.content.file,
                    contentDescription = null,
                    imageLoader = imageLoader,
                    modifier = Modifier.fillMaxSize()
                )
            }
            is MediaContent.Thumbnail -> {
                LaunchedEffect(Unit) {
                    content.content.downloadMedia()
                }
                Image(
                    bitmap = content.content.data,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
            is MediaContent.PlaceHolder -> {
                LaunchedEffect(Unit) {
                    content.content.downloadMedia()
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = date,
                fontSize = fontSize,
                lineHeight = fontSize,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}