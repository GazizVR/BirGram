package org.gaziz.birgram.ui.navigation

sealed class Route(val route: String) {
    object Auth: Route("auth")
    object ChatList: Route("chatList")
    object SearchChats: Route("searchChats")
}