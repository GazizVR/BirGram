package org.gaziz.birgram.data.remote

import android.util.Log
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
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
        onError: (String) -> Unit,
        onOk: () -> Unit = {},
    ) {
        if(client == null) {
            Log.e("TDLib", "Client is null")
            onError("TDLib Client is null")
            return
        }
        client?.send(
            query,
            {
                when (it.javaClass) {
                    TdApi.Error::class.java -> {
                        val err = it as TdApi.Error
                        Log.e("TDLib", "${err.code}, ${err.message}")
                        onError(err.message)
                    }
                    TdApi.Ok::class.java -> onOk()
                }
            },
            {
                val message = it.localizedMessage ?: it.message ?: "unknown request exception"
                Log.e("TDLib", message)
                onError(message)
            }
        )
    }
}