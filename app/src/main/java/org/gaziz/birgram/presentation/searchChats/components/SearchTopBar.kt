package org.gaziz.birgram.presentation.searchChats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import org.gaziz.birgram.R

@Composable
fun SearchTopBar(
    onBack: () -> Unit
){
    Row(
        modifier = Modifier.statusBarsPadding(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        IconButton(
            onClick = {onBack()}
        ) {
            Icon(
                ImageVector.vectorResource(R.drawable.arrow_back),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}