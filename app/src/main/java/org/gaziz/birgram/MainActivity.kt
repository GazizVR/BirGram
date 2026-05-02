package org.gaziz.birgram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gaziz.birgram.data.TDLibRepository
import org.gaziz.birgram.data.UserPreferencesRepository
import org.gaziz.birgram.presentation.theme.BirGramTheme
import org.gaziz.birgram.presentation.TGViewModel
import org.gaziz.birgram.presentation.TGViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userPreferencesRepository = UserPreferencesRepository(this)
        val tdLibRepository = TDLibRepository(this)
        enableEdgeToEdge()
        setContent {
            val tgViewModel = viewModel<TGViewModel>(
                factory = TGViewModelFactory(
                    userPreferencesRepository,
                    tdLibRepository
                )
            )
            val isDarkTheme by tgViewModel.isDarkTheme.collectAsState()
            BirGramTheme(isDarkTheme) {
                App(tgViewModel)
            }
        }
    }

}