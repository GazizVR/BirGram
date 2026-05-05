package org.gaziz.birgram.data.remote

import android.util.Log
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.domain.model.chatList.RequestResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TelegramManager @Inject constructor(){
    var client: Client? = null

    fun createClient(
        updateHandler: (TdApi.Object) -> Unit,
        exceptionHandler: (Throwable) -> Unit
    ) {
        client = Client.create(
            { updateHandler(it) },
            { exceptionHandler(it) },
            null
        )
    }

    fun sendRequest(
        query: TdApi.Function<*>,
        onError: (RequestResponse.Error?) -> Unit,
    ) {
        if(client == null) {
            Log.e("TDLib", "Client is null")
            onError(RequestResponse.Error(500,"Client is null"))
            return
        }
        onError(null)
        client?.send(
            query,
            {
                if(it is TdApi.Error) {
                    Log.e("TDLib", "${it.code}, ${it.message}")
                    onError(RequestResponse.Error(it.code,it.message))
                }
            },
            {
                val message = it.localizedMessage ?: it.message ?: "unknown request exception"
                Log.e("TDLib", message)
                onError(RequestResponse.Error(500,message))
            }
        )
    }
}