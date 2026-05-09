package org.gaziz.birgram.data.remote

import android.util.Log
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.domain.model.RequestResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TelegramManager @Inject constructor(){
    var client: Client? = null

    companion object {
        const val LOG_TAG = "BG"
    }

    val logTag = LOG_TAG

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
        onResult: (TdApi.Object) -> Unit = {}
    ) {
        if(client == null) {
            Log.e(LOG_TAG, "Client is null")
            onError(RequestResponse.Error(500,"Client is null"))
            return
        }
        onError(null)
        client?.send(
            query,
            {
                onResult(it)
                if(it is TdApi.Error) {
                    Log.e(LOG_TAG, "${it.code}, ${it.message}")
                    onError(RequestResponse.Error(it.code,it.message))
                }
            },
            {
                val message = it.localizedMessage ?: it.message ?: "unknown request exception"
                Log.e(LOG_TAG, message)
                onError(RequestResponse.Error(500,message))
            }
        )
    }
}