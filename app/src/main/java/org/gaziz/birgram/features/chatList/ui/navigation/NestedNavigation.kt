package org.gaziz.birgram.features.chatList.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.gaziz.birgram.core.navigation.ChatListRoute
import org.gaziz.birgram.core.navigation.ChatRoute
import org.gaziz.birgram.core.navigation.SearchChatsRoute
import org.gaziz.birgram.features.chatList.ui.screen.ArchiveScreen
import org.gaziz.birgram.features.chatList.ui.screen.MainScreen

fun NavGraphBuilder.chatListGraph(
    navController: NavController
){
    navigation<ChatListRoute>(
        startDestination = MainRoute
    ) {
        composable<MainRoute>(
            popEnterTransition = {
                expandHorizontally(
                    animationSpec = tween(),
                )
            },
            exitTransition = {
                shrinkHorizontally(
                    animationSpec = tween(),
                )
            },
        ) {
            MainScreen(
                onSearchClick = { navController.navigate(SearchChatsRoute) },
                onChatClick = { navController.navigate(ChatRoute(it)) },
                onArchiveClick = { navController.navigate(ArchiveRoute) }
            )
        }
        composable<ArchiveRoute>(
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(),
                    initialOffsetX = {it}
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(),
                    targetOffsetX = {it}
                )
            },
        ) {
            ArchiveScreen(
                onBack = { navController.popBackStack() },
                onChatClick = { navController.navigate(ChatRoute(it)) },
            )
        }
    }
}