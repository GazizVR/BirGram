package org.gaziz.birgram.features.chat.ui.component.messageContent

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_TEXTURE_VIEW
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import org.gaziz.birgram.R
import org.gaziz.birgram.features.chat.ui.model.MediaContent

@Composable
fun MediaPreview(
    content: MediaContent?,
    caption: String? = null,
    width: Int,
    height: Int,
    containerColor: Color,
    date: String,
    fontSize: TextUnit,
    overlay: @Composable () -> Unit = {}
) {
    val shape = RoundedCornerShape(18.dp)
    Box(
        modifier = Modifier
            .clip(shape)
            .border(1.dp,containerColor,shape)
            .background(containerColor)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            BoxWithConstraints {
                val scaleW = maxWidth/width
                val scaleH = maxHeight/height
                val scale = minOf(1.dp,scaleW,scaleH)
                Box(
                    modifier = Modifier
                        .width(width*scale)
                        .height(height*scale),
                    contentAlignment = Alignment.Center
                ) {
                    when(content) {
                        is MediaContent.Image -> {
                            if(content.isGIF) {
                                val context = LocalContext.current
                                val imageLoader = remember(context) {
                                    ImageLoader.Builder(context)
                                        .components {
                                            if (SDK_INT >= 28) {
                                                add(AnimatedImageDecoder.Factory())
                                            } else {
                                                add(GifDecoder.Factory())
                                            }
                                        }
                                        .build()
                                }
                                AsyncImage(
                                    model = content.file,
                                    contentDescription = null,
                                    imageLoader = imageLoader,
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                AsyncImage(
                                    model = content.file,
                                    contentDescription = null,
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        is MediaContent.Video -> {
                            val context = LocalContext.current
                            val player = remember {
                                ExoPlayer
                                    .Builder(context)
                                    .build()
                                    .apply {
                                        repeatMode = Player.REPEAT_MODE_ALL
                                        playWhenReady = true
                                        setMediaItem(
                                            MediaItem.fromUri(
                                                getUriForFile(context,content.file)
                                            )
                                        )
                                        prepare()
                                    }
                            }
                            DisposableEffect(Unit) {
                                onDispose {
                                    player.release()
                                }
                            }
                            PlayerSurface(
                                player = player,
                                modifier = Modifier.fillMaxSize(),
                                surfaceType = SURFACE_TYPE_TEXTURE_VIEW
                            )
                        }
                        is MediaContent.Thumbnail -> {
                            LaunchedEffect(Unit) {
                                content.downloadMedia()
                            }
                            Image(
                                bitmap = content.data,
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        is MediaContent.PlaceHolder -> {
                            LaunchedEffect(Unit) {
                                content.downloadMedia()
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(containerColor)
                            )
                        }
                        null -> {
                            val notFound = stringResource(R.string.not_found)
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(containerColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = notFound,
                                    fontSize = 7.sp,
                                    lineHeight = 7.sp,
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                    if(caption == null) {
                        Box(
                            modifier = Modifier.fillMaxSize().padding(8.dp),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.background.copy(0.35f),
                                        RoundedCornerShape(20.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
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
                    overlay()
                }
            }
            if(caption != null) {
                val textSize = 6.sp
                TextPreview(
                    text = caption,
                    isSpacer = true,
                    date = date,
                    fontSize = textSize,
                    containerColor = containerColor,
                    senderInfo = null
                )
            }
        }
    }
}