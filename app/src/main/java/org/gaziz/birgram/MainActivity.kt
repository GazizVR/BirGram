package org.gaziz.birgram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gaziz.birgram.data.remote.Auth
import org.gaziz.birgram.data.local.UserPreferences
import org.gaziz.birgram.presentation.theme.BirGramTheme
import org.gaziz.birgram.presentation.TGViewModel
import org.gaziz.birgram.presentation.TGViewModelFactory
import org.gaziz.birgram.ui.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userPreferences = UserPreferences(this)
        val tdLib = Auth(this)
        enableEdgeToEdge()
        setContent {
            val tgViewModel = viewModel<TGViewModel>(
                factory = TGViewModelFactory(
                    userPreferences,
                    tdLib
                )
            )
            val isDarkTheme by tgViewModel.isDarkTheme.collectAsState()
            BirGramTheme(isDarkTheme) {
                App(tgViewModel)
            }
        }
    }

}