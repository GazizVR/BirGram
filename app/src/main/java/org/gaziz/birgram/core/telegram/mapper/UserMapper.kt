package org.gaziz.birgram.core.telegram.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.model.UserStatus
import org.gaziz.birgram.features.chat.data.mapper.fromUnixTimeStamp

fun TdApi.UserStatus.toStatus(): UserStatus {
   return when(this){
       is TdApi.UserStatusRecently -> UserStatus.Recently
       is TdApi.UserStatusLastWeek -> UserStatus.LastWeek
       is TdApi.UserStatusLastMonth -> UserStatus.LastMonth
       is TdApi.UserStatusOffline -> UserStatus.Offline(this.wasOnline.fromUnixTimeStamp())
       is TdApi.UserStatusOnline -> UserStatus.Online(this.expires.fromUnixTimeStamp())
       else -> UserStatus.Empty
   }
}