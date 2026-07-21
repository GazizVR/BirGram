package org.gaziz.birgram.features.chat.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.core.telegram.api.model.chat.ChatPhoto
import org.gaziz.birgram.core.telegram.api.model.chat.ChatType
import org.gaziz.birgram.core.telegram.api.model.user.UserStatus
import org.gaziz.birgram.core.ui.components.ChatTypeCnt
import org.gaziz.birgram.core.ui.icons.arrowBack
import org.gaziz.birgram.core.ui.icons.moreVert

@Composable
fun ChatTopBar(
    photo: ChatPhoto?,
    title: String,
    type: ChatType,
    onBack: () -> Unit,
    userStatus: @Composable (Long) -> UserStatus?
){
    val iconSize = 40.dp
    val cardColor = TopAppBarDefaults.topAppBarColors().containerColor
    Card(
        colors = CardDefaults.cardColors().copy(containerColor = cardColor),
        modifier = Modifier.statusBarsPadding(),
        shape = RoundedCornerShape(0.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = arrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 8.sp
                )
                ChatTypeCnt(type,6.sp, userStatus)
            }
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = moreVert,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}