package org.gaziz.birgram.features.chat.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.video.VideoFrameDecoder
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.launch
import org.gaziz.birgram.features.chat.ui.model.StickerContent

@Composable
fun MessageStickerPreview(
    modifier: Modifier = Modifier,
    content: StickerContent,
    date: String,
    datePadding: Dp = 0.dp,
    fontSize: TextUnit,
    containerColor: Color
) {
    Box(
        modifier = modifier
            .background(if(content is StickerContent.Empty) containerColor else Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        when(content) {
            is StickerContent.Picture -> {
                AsyncImage(
                    model = content.file,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
            is StickerContent.Animation -> {
                val composition by rememberLottieComposition(LottieCompositionSpec.File(content.path))
                val animatable = rememberLottieAnimatable()
                val scope = rememberCoroutineScope()
                var isPlaying by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    if(!isPlaying) {
                        isPlaying = true
                        scope.launch {
                            animatable.animate(composition)
                            isPlaying = false
                        }
                    }
                }
                LottieAnimation(
                    composition = composition,
                    progress = { animatable.progress },
                    modifier = Modifier.fillMaxSize().clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ){
                        if(!isPlaying) {
                            isPlaying = true
                            scope.launch {
                                animatable.animate(composition)
                                isPlaying = false
                            }
                        }
                    }
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
                    model = content.path,
                    contentDescription = null,
                    imageLoader = imageLoader,
                    modifier = Modifier.fillMaxSize()
                )
            }
            is StickerContent.Empty -> {
                LaunchedEffect(Unit) {
                    content.downloadContent()
                }
                Text(
                    text = content.emoji,
                    textAlign = TextAlign.Center
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxSize().padding(datePadding),
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