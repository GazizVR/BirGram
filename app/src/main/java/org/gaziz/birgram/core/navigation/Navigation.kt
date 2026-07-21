package org.gaziz.birgram.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.gaziz.birgram.features.auth.ui.AuthScreen
import org.gaziz.birgram.features.chat.ui.ChatScreen
import org.gaziz.birgram.features.chatList.ui.ChatListScreen
import org.gaziz.birgram.features.searchChats.ui.SearchChatsScreen
import org.gaziz.birgram.features.splash.ui.SplashScreen

@Composable
fun Navigation(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = SplashRoute
    ){
        composable<SplashRoute> {
            SplashScreen(
                onReady =  { navController.navigate(ChatListRoute) },
                onAuth = { navController.navigate(AuthRoute) },
                onNonReady = {
                    val currentBack = navController.currentBackStackEntry
                    if(currentBack?.destination != SplashRoute) {
                        navController.popBackStack(SplashRoute, inclusive = false)
                    }
                }
            )
        }
        composable<AuthRoute> {
            AuthScreen(
                onReady =  { navController.navigate(ChatListRoute) },
                onLogOut = { navController.navigate(SplashRoute) }
            )
        }
        composable<ChatListRoute> {
            ChatListScreen(
                { navController.navigate(SearchChatsRoute) },
                { navController.navigate(ChatRoute(it)) },
            )
        }
        composable<SearchChatsRoute> {
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