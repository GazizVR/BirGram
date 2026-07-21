package org.gaziz.birgram.features.chatList.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import org.gaziz.birgram.R
import org.gaziz.birgram.core.ui.icons.darkMode
import org.gaziz.birgram.core.ui.icons.exitToApp

@Composable
fun MainScreenMenu(
    isDark: Boolean,
    switchIsDark: (Boolean) -> Unit,
    onLogOut: () -> Unit
) {
    val cnt = stringArrayResource(R.array.chat_list_menu_cnt)
    ModalDrawerSheet(
        drawerShape = RoundedCornerShape(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ){
            Row(
                modifier = Modifier.clickable { switchIsDark(!isDark) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = darkMode,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text= cnt[0],
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Switch(
                    checked = isDark,
                    onCheckedChange = switchIsDark,
                )
            }

            Row(
                modifier = Modifier.clickable { onLogOut() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = exitToApp,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text= cnt[1],
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}