package org.gaziz.birgram.core.telegram.users.impl

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.chats.impl.fromUnixTimeStamp
import org.gaziz.birgram.core.telegram.users.api.model.User
import org.gaziz.birgram.core.telegram.users.api.model.UserStatus
import org.gaziz.birgram.core.telegram.users.api.model.UserType

fun TdApi.UserStatus.toStatus(): UserStatus {
   return when(this){
       is TdApi.UserStatusRecently -> UserStatus.Recently(this.byMyPrivacySettings)
       is TdApi.UserStatusLastWeek -> UserStatus.LastWeek
       is TdApi.UserStatusLastMonth -> UserStatus.LastMonth
       is TdApi.UserStatusOffline -> UserStatus.Offline(this.wasOnline.fromUnixTimeStamp())
       is TdApi.UserStatusOnline -> UserStatus.Online(this.expires.fromUnixTimeStamp())
       else -> UserStatus.Empty
   }
}

fun TdApi.UserType.toType(): UserType {
    return when(this) {
        is TdApi.UserTypeBot -> UserType.Bot
        is TdApi.UserTypeDeleted -> UserType.Deleted
        is TdApi.UserTypeRegular -> UserType.Regular
        else -> UserType.Unknown
    }
}

fun TdApi.User.toUser(): User {
    return User(
        id = this.id,
        status = this.status.toStatus(),
        type = this.type.toType()
    )
}