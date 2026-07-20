package org.gaziz.birgram.features.chatList.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@Composable
fun ChatCard(
    modifier: Modifier,
    photoModel: Any? = null,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainer),
        shape = RoundedCornerShape(0.dp),
        onClick = onClick
    ) {
        Row(
            modifier = modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier.size(60.dp)
            ) {
                if(photoModel != null) {
                    AsyncImage(
                        model = photoModel,
                        contentDescription = null,
                        modifier = Modifier
                            .size(55.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(55.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    )
                }
            }
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 7.sp,
            )
        }
    }
}