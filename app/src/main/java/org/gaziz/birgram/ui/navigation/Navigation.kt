package org.gaziz.birgram.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.gaziz.birgram.presentation.auth.screen.AuthScreen
import org.gaziz.birgram.presentation.chat.screen.ChatScreen
import org.gaziz.birgram.presentation.chatList.screen.ChatListScreen
import org.gaziz.birgram.presentation.searchChats.screen.SearchChatsScreen

@Composable
fun Navigation(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Route.Auth.route
    ){
        composable(Route.Auth.route) { AuthScreen { navController.navigate(Route.ChatList.route) } }
        composable(Route.ChatList.route) {
            ChatListScreen(
                { navController.navigate(Route.SearchChats.route) },
                { navController.navigate(ChatRoute(it)) }
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