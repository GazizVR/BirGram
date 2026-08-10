package org.gaziz.birgram.features.chat.ui.component.messageContent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.video.VideoFrameDecoder
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import org.gaziz.birgram.features.chat.ui.component.PlaybackButton
import org.gaziz.birgram.features.chat.ui.model.StickerContent
import java.io.File

@Composable
fun StickerPreview(
    modifier: Modifier = Modifier,
    content: StickerContent,
    date: String,
    datePadding: Dp = 0.dp,
    fontSize: TextUnit,

    player: ExoPlayer? = null,
    currentMedia: File? = null,
    onVideoClick: (File) -> Unit = {}
) {
    Box(
        modifier = modifier,
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
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.File(content.path)
                )
                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    iterations = LottieConstants.IterateForever
                )
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.fillMaxSize()
                )
            }
            is StickerContent.Video -> {
                val context = LocalContext.current
                if(
                    content.file == currentMedia &&
                    player != null
                ) {
                    PlayerSurface(
                        player = player,
                        modifier = Modifier.fillMaxSize(),
                        surfaceType = SURFACE_TYPE_SURFACE_VIEW
                    )
                } else {
                    val interactionSource = remember { MutableInteractionSource() }
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(content.file)
                            .decoderFactory(VideoFrameDecoder.Factory())
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                indication = null,
                                interactionSource = interactionSource
                            ) {
                                onVideoClick(content.file)
                            }
                    )
                    PlaybackButton()
                }
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
            modifier = Modifier
                .fillMaxSize()
                .padding(datePadding),
            contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.background.copy(0.35f),
                        RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = date,
                    modifier = Modifier.padding(
                        vertical = 2.dp,
                        horizontal = 4.dp
                    ),
                    fontSize = fontSize,
                    lineHeight = fontSize,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}