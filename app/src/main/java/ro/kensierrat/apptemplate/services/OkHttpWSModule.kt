package ro.kensierrat.apptemplate.services

import android.os.Handler
import androidx.activity.ComponentActivity
import kotlinx.coroutines.InternalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ro.kensierrat.apptemplate.MainActivity
import ro.kensierrat.apptemplate.server.ServerApiHelper
import ro.kensierrat.apptemplate.server.ServerBridgeCoroutine

class OkHttpWSModule private constructor() {
    companion object {
        @Volatile
        private var instance : OkHttpWSModule? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getInstance() =
            instance ?: kotlinx.coroutines.internal.synchronized(this) {
                instance ?: OkHttpWSModule().also { instance = it }
            }
    }

    private val client = OkHttpClient()
    private val request = Request.Builder().url("ws://10.0.2.2:2528").build()
    private var parentHandler: Handler? = null
    private var parentContext: ComponentActivity? = null
    private var listener: RagtagWebSocketListener? = null
    public var socket: WebSocket? = null

    public fun initWebSocketInfo(handler: Handler, context: ComponentActivity) {
        if (parentContext == null && parentHandler == null) {
            parentHandler = handler
            parentContext = context
        }
    }

    public fun initWebSocket() {
        if (listener == null && socket == null) {
            listener = RagtagWebSocketListener(parentHandler!!, parentContext!!)
            socket = client.newWebSocket(request, listener!!)
        }
    }
}