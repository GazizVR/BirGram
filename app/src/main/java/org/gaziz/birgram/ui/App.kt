package org.gaziz.birgram.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import org.gaziz.birgram.ui.navigation.Navigation
import org.gaziz.birgram.ui.theme.BirGramTheme

@Composable
fun App() {
    val navController = rememberNavController()
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()
    BirGramTheme(isDarkTheme) {
        Navigation(navController)
    }
}