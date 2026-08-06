package org.gaziz.birgram.features.chat.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import coil3.video.VideoFrameDecoder
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import org.gaziz.birgram.features.chat.ui.model.StickerContent

@Composable
fun MessageStickerPreview(
    modifier: Modifier = Modifier,
    content: StickerContent,
    date: String,
    fontSize: TextUnit,
    containerColor: Color
) {
    val shape = RoundedCornerShape(18.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .background(if(content is StickerContent.Empty) containerColor else Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        when(val cnt = content) {
            is StickerContent.Picture -> {
                AsyncImage(
                    model = cnt.file,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
            is StickerContent.Animation -> {
                val composition by rememberLottieComposition(LottieCompositionSpec.File(cnt.path))
                val progress by animateLottieCompositionAsState(composition)
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.fillMaxSize()
                )
            }
            is StickerContent.Video -> {
                val context = LocalContext.current
                val imageLoader = remember(context) {
                    ImageLoader.Builder(context)
                        .components { add(VideoFrameDecoder.Factory()) }
                        .build()
                }
                AsyncImage(
                    model = cnt.path,
                    contentDescription = null,
                    imageLoader = imageLoader,
                    modifier = Modifier.fillMaxSize()
                )
            }
            is StickerContent.Empty -> {
                LaunchedEffect(Unit) {
                    cnt.downloadContent()
                }
                Text(
                    text = cnt.emoji,
                    textAlign = TextAlign.Center
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(7.dp),
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