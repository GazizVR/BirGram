package org.gaziz.birgram.core.telegram

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.model.RequestResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TelegramManager @Inject constructor(){
    var client: Client? = null

    companion object {
        const val LOG_TAG = "TGMANAGER"
        const val DEFAULT_CODE_LENGTH = 5
    }

    fun getLogTag(): String {
        return LOG_TAG
    }

    fun getDefaultCodeLength(): Int {
        return DEFAULT_CODE_LENGTH
    }

    private val _update = MutableSharedFlow<TdApi.Object>(
        0,
        128,
        BufferOverflow.SUSPEND
    )
    val update = _update.asSharedFlow()

    private val _exception = MutableStateFlow<Throwable?>(null)
    val exception = _exception.asStateFlow()

    fun createClient() {
        client = Client.create(
            { u ->
                CoroutineScope(Dispatchers.IO).launch {
                    _update.emit(u)
                }
            },
            { e -> _exception.update { e } },
            null
        )
    }

    fun sendRequest(
        query: TdApi.Function<*>,
        onError: (RequestResponse.Error) -> Unit = {},
        onResult: (TdApi.Object) -> Unit = {}
    ) {
        if(client == null) {
            Log.e(LOG_TAG, "Client is null")
            onError(RequestResponse.Error(500,"Client is null"))
            return
        }
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