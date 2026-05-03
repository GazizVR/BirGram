package org.gaziz.birgram.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.gaziz.birgram.presentation.auth.screen.AuthScreen

@Composable
fun Navigation(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Route.Auth.route
    ){
        composable(Route.Auth.route){
            AuthScreen()
        }
    }
}

sealed class Route(val route: String) {
    object Auth: Route("auth")
}
