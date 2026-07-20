package org.gaziz.birgram.core.telegram.internal

import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.model.ResponseData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientManager @Inject constructor(){
    var client: Client? = null

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
        client?.send(
            TdApi.SetLogVerbosityLevel().apply {
                this.newVerbosityLevel = 0
            }
        ) {}
        client?.send(
            TdApi.SetLogStream().apply {
                this.logStream = TdApi.LogStreamEmpty()
            }
        ){}
    }

    fun sendRequest(
        query: TdApi.Function<*>,
        onError: (ResponseData.Error) -> Unit = {},
        onResult: (TdApi.Object) -> Unit = {}
    ) {
        if(client == null) {
            onError(ResponseData.Error(500,"Client is null"))
            return
        }
        client?.send(
            query,
            {
                onResult(it)
                if(it is TdApi.Error) {
                    onError(ResponseData.Error(it.code,it.message))
                }
            },
            {
                val message = it.localizedMessage ?: it.message ?: "unknown request exception"
                onError(ResponseData.Error(500,message))
            }
        )
    }
}