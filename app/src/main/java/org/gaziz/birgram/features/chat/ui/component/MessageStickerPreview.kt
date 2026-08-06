package org.gaziz.birgram.features.chat.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import org.gaziz.birgram.features.chat.ui.model.MessageContentInfo
import org.gaziz.birgram.features.chat.ui.model.StickerContent

@Composable
fun MessageStickerPreview(
    info: MessageContentInfo.Sticker,
    date: String,
    fontSize: TextUnit,
    containerColor: Color
) {
    val shape = RoundedCornerShape(20.dp)
    Box(
        modifier = Modifier
            .size(150.dp)
            .clip(shape)
            .background(if(info.content is StickerContent.Empty) containerColor else Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        when(val cnt = info.content) {
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
            is StickerContent.Empty -> {
                LaunchedEffect(Unit) {
                    cnt.downloadContent()
                }
                Text(
                    text = info.emoji,
                    textAlign = TextAlign.Center
                )
            }
            else -> {

            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
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