package org.gaziz.birgram.feature.chatlist.ui.component

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.gaziz.birgram.core.ui.icon.menu
import org.gaziz.birgram.core.ui.icon.search

@Composable
fun MainScreenTopBar(
    navigateToSearch: () -> Unit,
    onMenu: () -> Unit
) {
    val context = LocalContext.current
    val label = remember(context) {
        val pm = context.packageManager
        context.applicationInfo.loadLabel(pm).toString()
    }
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
            text = label,
            style = MaterialTheme.typography.labelSmall
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