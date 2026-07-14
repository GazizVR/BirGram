package org.gaziz.birgram.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.gaziz.birgram.features.auth.ui.AuthScreen
import org.gaziz.birgram.features.splash.ui.SplashScreen
import org.gaziz.birgram.presentation.chat.screen.ChatScreen
import org.gaziz.birgram.presentation.chatList.screen.ChatListScreen
import org.gaziz.birgram.presentation.searchChats.screen.SearchChatsScreen

@Composable
fun Navigation(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Route.Splash.route
    ){
        composable(Route.Splash.route) {
            SplashScreen(
                onReady =  { navController.navigate(Route.ChatList.route) },
                onAuth = { navController.navigate(Route.Auth.route) }
            )
        }
        composable(Route.Auth.route) {
            AuthScreen(
                onReady =  { navController.navigate(Route.ChatList.route) },
                onLogOut = { navController.navigate(Route.Splash.route) }
            )
        }
        composable(Route.ChatList.route) {
            ChatListScreen(
                { navController.navigate(Route.SearchChats.route) },
                { navController.navigate(ChatRoute(it)) },
                { navController.navigate(Route.Splash.route) }
            )
        }
        composable(Route.SearchChats.route) {
            SearchChatsScreen(
                { navController.popBackStack() },
                { navController.navigate(ChatRoute(it)) }
            )
        }
        composable<ChatRoute> { backStackEntry ->
            val chat = backStackEntry.toRoute<ChatRoute>()
            ChatScreen(chat.chatId) { navController.popBackStack() }
        }
    }
}