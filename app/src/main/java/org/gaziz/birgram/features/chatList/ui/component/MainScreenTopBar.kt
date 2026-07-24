package org.gaziz.birgram.features.chatList.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.gaziz.birgram.R
import org.gaziz.birgram.core.ui.icon.menu
import org.gaziz.birgram.core.ui.icon.search

@Composable
fun MainScreenTopBar(
    navigateToSearch: () -> Unit,
    onMenu: () -> Unit
) {
    val appName = stringResource(R.string.app_name)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(WindowInsets.statusBars.asPaddingValues())
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onMenu,
        ) {
            Icon(
                imageVector = menu,
                contentDescription = "menu",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(26.dp)
            )
        }
        Text(
            appName,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelLarge
        )
        IconButton(
            onClick = { navigateToSearch() },
        ) {
            Icon(
                imageVector = search,
                contentDescription = "menu",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}