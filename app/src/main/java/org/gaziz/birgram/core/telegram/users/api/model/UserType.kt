package org.gaziz.birgram.core.telegram.users.api.model

sealed interface UserType {
    object Bot: UserType
    object Deleted: UserType
    object Regular: UserType
    object Unknown: UserType
}