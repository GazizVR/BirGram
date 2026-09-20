package org.gaziz.telegram.api.model.message

sealed interface Origin {
    data class Channel(val id: Long): Origin
    data class Chat(val id: Long): Origin
    data class HiddenUser(val name: String): Origin
    data class User(val id: Long): Origin
    object Other: Origin
}