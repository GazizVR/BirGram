package org.gaziz.birgram.core.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.gaziz.birgram.features.auth.ui.AuthScreen
import org.gaziz.birgram.features.chat.ui.ChatScreen
import org.gaziz.birgram.features.chatList.ui.navigation.chatListGraph
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
                onReady =  {
                    navController.navigate(ChatListRoute) {
                        popUpTo(SplashRoute) {
                           inclusive = true
                        }
                    }
                },
                onAuth = {
                    navController.navigate(AuthRoute) {
                        popUpTo(SplashRoute) {
                            inclusive = true
                        }
                    }
                },
                onNonReady = {
                    navController.popBackStack(SplashRoute, inclusive = false)
                }
            )
        }
        composable<AuthRoute> {
            AuthScreen(
                onReady =  {
                    navController.navigate(ChatListRoute){
                        popUpTo(AuthRoute) {
                            inclusive = true
                        }
                    }
                },
            )
        }
        chatListGraph(navController)
        composable<SearchChatsRoute>(
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(),
                    initialOffsetX = {it}
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(),
                    targetOffsetX = {it}
                )
            },
        ) {
            SearchChatsScreen(
                {
                    val backStackEntry = navController.previousBackStackEntry
                    if(backStackEntry != null) {
                        navController.popBackStack()
                    }
                },
                { navController.navigate(ChatRoute(it)) }
            )
        }
        composable<ChatRoute>(
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(),
                    initialOffsetX = {it}
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(),
                    targetOffsetX = {it}
                )
            },
        ) { backStackEntry ->
            val chat = backStackEntry.toRoute<ChatRoute>()
            ChatScreen(chat.chatId) {
                val backStackEntry = navController.previousBackStackEntry
                if(backStackEntry != null) {
                    navController.popBackStack()
                }
            }
        }
    }
}