package org.gaziz.birgram.presentation.chatList.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatFolderSection(
    isSelected: Boolean = false,
    onSelected: () -> Unit,
    title: String,
    icon: ImageVector,
    unreadCount: Int
){
    val contentHeight = 40.dp
    val color by animateColorAsState(if(isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(0.5f))
    var width by rememberSaveable { mutableIntStateOf(0) }
    Box(
        modifier = Modifier.clickable{ onSelected() }
    ){
        Column(
            modifier = Modifier.padding(8.dp).fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.onGloballyPositioned{width = it.size.width}
            ) {
                Icon(
                    imageVector = icon,
                    tint = color,
                    contentDescription = title,
                    modifier = Modifier.size(24.dp),
                )
                Spacer(Modifier.width(6.dp))
                if(isSelected){
                    Text(
                        text = title,
                        color = color,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                if(unreadCount > 0) {
                    Spacer(Modifier.width(2.dp))
                    Box(
                        modifier =
                            Modifier
                                .clip(CircleShape)
                                .background(color)
                                .size((contentHeight / 2) + 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$unreadCount",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 8.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            if(isSelected){
                Spacer(Modifier.weight(1f))
                HorizontalDivider(
                    thickness = 3.dp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.width((width/3).dp)
                )
            }
        }
    }
}