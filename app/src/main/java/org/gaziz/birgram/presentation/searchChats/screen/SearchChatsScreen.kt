package org.gaziz.birgram.presentation.searchChats.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.gaziz.birgram.presentation.searchChats.components.SearchTopBar

@Composable
fun SearchChatsScreen(
    onBack: () -> Unit
){
    Scaffold(
        topBar = { SearchTopBar(onBack) }
    ) {
        LazyColumn(Modifier.padding(it)) {

        }
    }
}