package org.gaziz.birgram.features.chatList.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.gaziz.birgram.core.navigation.ChatListRoute
import org.gaziz.birgram.core.navigation.ChatRoute
import org.gaziz.birgram.core.navigation.SearchChatsRoute
import org.gaziz.birgram.features.chatList.ui.ArchiveScreen
import org.gaziz.birgram.features.chatList.ui.MainScreen

fun NavGraphBuilder.chatListGraph(
    navController: NavController
){
    navigation<ChatListRoute>(
        startDestination = MainRoute
    ) {
        composable<MainRoute> {
            MainScreen(
                onSearchClick = { navController.navigate(SearchChatsRoute) },
                onChatClick = { navController.navigate(ChatRoute(it)) },
                onArchiveClick = { navController.navigate(ArchiveRoute) }
            )
        }
        composable<ArchiveRoute> {
            ArchiveScreen(
                onBack = { navController.popBackStack(MainRoute, inclusive = false) },
                onChatClick = { navController.navigate(ChatRoute(it)) },
            )
        }
    }
}