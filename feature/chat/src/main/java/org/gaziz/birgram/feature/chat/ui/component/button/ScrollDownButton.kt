package org.gaziz.birgram.feature.chat.ui.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.core.ui.icon.arrowDownwardAlt
import org.gaziz.birgram.core.ui.theme.BirGramTheme

@Composable
fun ScrollDownButton(
    onClick: () -> Unit,
    unreadCount: Int? = null
) {
    val fontSize = 6.sp
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                contentColor = MaterialTheme.colorScheme.onBackground.copy(0.75f)
            ),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(48.dp).clip(CircleShape)
        ) {
            Icon(
                imageVector = arrowDownwardAlt,
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
        }
        if(unreadCount != null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "$unreadCount",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = fontSize,
                    lineHeight = fontSize,
                    modifier = Modifier.padding(
                        horizontal = if(unreadCount < 10) 5.dp else 3.dp,
                        vertical = 2.dp
                    ),
                    maxLines = 1
                )
            }
        }
    }
}

@Preview
@Composable
fun ScrollDownBtnPrv(){
    BirGramTheme(true) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            ScrollDownButton(
                {},
                1
            )
        }
    }
}
