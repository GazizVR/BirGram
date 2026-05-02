package org.gaziz.birgram.ui.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.gaziz.birgram.presentation.TGViewModel
import org.gaziz.birgram.presentation.auth.components.PasswordRecovery
import org.gaziz.birgram.presentation.auth.screen.AuthScreen
import org.gaziz.birgram.presentation.chatList.screen.ChatListScreen

@Composable
fun Navigation(
    navController: NavHostController,
    tgViewModel: TGViewModel,
    padding: PaddingValues
){
    NavHost(
        navController = navController,
        startDestination = Route.Auth.route
    ){
        composable(
            route = Route.PasswordRecovery.route,
            enterTransition = { slideInHorizontally{-it} }
        ) {
            PasswordRecovery()
        }
        composable(
            route = Route.Auth.route
        ){
            AuthScreen(tgViewModel,navController)
        }
        composable(
            route = Route.Chats.route
        ) {
            ChatListScreen(tgViewModel,padding)
        }
    }
}

sealed class Route(val route: String) {
    object Auth: Route("login")
    object Chats: Route("chats")
    object PasswordRecovery: Route("passwordRecovery")
}
