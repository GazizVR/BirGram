package org.gaziz.birgram.feature.chat.ui.component.messageContent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.feature.chat.R

@Composable
fun ForwardedText(
    senderName: String,
    isOutgoing: Boolean,
    fontSize: TextUnit = 5.sp
) {
    val color = if(isOutgoing) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.primary
    val prefix = stringResource(R.string.forwarded_from)
    FlowRow(
        maxLines = 2,
        maxItemsInEachRow = 1,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = prefix,
            maxLines = 1,
            color = color,
            overflow = TextOverflow.Ellipsis,
            fontSize = fontSize,
            lineHeight = fontSize,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = senderName,
            color = color,
            maxLines = 1,
            fontSize = fontSize,
            lineHeight = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ForwardedBadge(
    senderName: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background.copy(0.5f)
        )
    ) {
        Box(
            modifier = Modifier.padding(3.dp)
        ) {
            ForwardedText(senderName,true)
        }
    }
}