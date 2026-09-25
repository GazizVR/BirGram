package org.gaziz.telegram.api.model.message

sealed interface MessageOrigin {
    data class Channel(val id: Long): MessageOrigin
    data class Chat(val id: Long): MessageOrigin
    data class HiddenUser(val name: String): MessageOrigin
    data class User(val id: Long): MessageOrigin
    object Other: MessageOrigin
}