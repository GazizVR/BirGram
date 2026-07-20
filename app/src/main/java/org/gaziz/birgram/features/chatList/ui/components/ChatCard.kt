package org.gaziz.birgram.features.chatList.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.gaziz.birgram.R

@Composable
fun ChatCard(
    modifier: Modifier,
    isDeleted: Boolean,
    photoModel: Any? = null,
    title: String,
    onClick: () -> Unit
) {
    val photoSize = 54.dp
    val userTypeCnt = stringArrayResource(R.array.user_types)
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
            if(photoModel is String) {
                AsyncImage(
                    model = photoModel,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(photoSize)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(photoSize)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    if(isDeleted) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.skull),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    } else {
                        if(title.isNotBlank()) {
                            Text(
                                text = title[0].toString(),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = if(isDeleted) {
                    userTypeCnt[1]
                } else {
                    title
                },
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 7.sp,
            )
        }
    }
}