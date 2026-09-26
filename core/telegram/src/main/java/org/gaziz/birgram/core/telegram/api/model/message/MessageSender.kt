package org.gaziz.birgram.core.telegram.api.model.message

sealed interface MessageSender {
    data class Chat(val id: Long): MessageSender
    data class User(val id: Long): MessageSender
    object Other: MessageSender
}