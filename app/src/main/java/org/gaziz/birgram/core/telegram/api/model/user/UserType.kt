package org.gaziz.birgram.core.telegram.api.model.user

sealed interface UserType {
    object Bot: UserType
    object Deleted: UserType
    object Regular: UserType
    object Unknown: UserType
}