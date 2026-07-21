package org.gaziz.birgram.features.chatList.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.gaziz.birgram.R
import org.gaziz.birgram.core.ui.icons.arrowBack
import org.gaziz.birgram.core.ui.icons.moreVert

@Composable
fun ArchiveScreen(
    onBack: () -> Unit
) {
    val archivedChats = stringResource(R.string.archived_chats)
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBack
                ) {
                    Icon(
                        imageVector = arrowBack,
                        contentDescription = null
                    )
                }
                Text(
                    text = archivedChats,
                    style = MaterialTheme.typography.labelSmall
                )
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = moreVert,
                        contentDescription = null
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {}
    }
}