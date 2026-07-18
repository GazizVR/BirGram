package org.gaziz.birgram.core.telegram.internal

import android.util.Log
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.model.ResponseData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientManager @Inject constructor(){
    var client: Client? = null

    companion object {
        const val LOG_TAG = "TGMANAGER"
        const val DEFAULT_CODE_LENGTH = 5
    }

    fun getDefaultCodeLength(): Int {
        return DEFAULT_CODE_LENGTH
    }

    fun isClientActive(): Boolean {
        return client != null
    }

    fun createClient(
        onUpdate: (TdApi.Object) -> Unit,
        onException: (Throwable) -> Unit
    ) {
        client = Client.create(
            { onUpdate(it) },
            { onException(it) },
            null
        )
    }

    fun sendRequest(
        query: TdApi.Function<*>,
        onError: (ResponseData.Error) -> Unit = {},
        onResult: (TdApi.Object) -> Unit = {}
    ) {
        if(client == null) {
            Log.e(LOG_TAG, "Client is null")
            onError(ResponseData.Error(500,"Client is null"))
            return
        }
        client?.send(
            query,
            {
                onResult(it)
                if(it is TdApi.Error) {
                    Log.e(LOG_TAG, "${it.code}, ${it.message}")
                    onError(ResponseData.Error(it.code,it.message))
                }
            },
            {
                val message = it.localizedMessage ?: it.message ?: "unknown request exception"
                Log.e(LOG_TAG, message)
                onError(ResponseData.Error(500,message))
            }
        )
    }
}