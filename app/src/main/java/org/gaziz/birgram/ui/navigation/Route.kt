package org.gaziz.birgram.ui.navigation

sealed class Route(val route: String) {
    object Splash: Route("splash")
    object Auth: Route("auth")
    object ChatList: Route("chatList")
    object SearchChats: Route("searchChats")
}