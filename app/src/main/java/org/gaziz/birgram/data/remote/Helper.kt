package org.gaziz.birgram.data.remote

import android.util.Log
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi

object Helper {
    var client: Client? = null

    fun sendRequest(
        query: TdApi.Function<*>,
        onError: (String) -> Unit,
        onOk: () -> Unit = {},
        client: Client
    ) {
        client.send(
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