package ro.kensierrat.apptemplate.services

import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import ro.kensierrat.apptemplate.MainActivity

class RagtagWebSocketListener(val handler: Handler, val context: ComponentActivity) : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.i("WSCONN", "ESTABLISHED WEBSOCKET CONNECTION")
        // Send an initial message to the server if needed
        webSocket.send("Hello from client!")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.i("WSCONN", "RECEIVED FROM SERVER (AS TEXT): $text")
        handler.post{Toast.makeText(context, "New addition to the server: $text", Toast.LENGTH_LONG).show()}
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.i("WSCONN", "RECEIVED FROM SERVER (AS BINARY): $bytes")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.i("WSCONN","SEVERING WEBSOCKET CONNECTION: Code=$code, Reason=$reason")
        // It’s good practice to close the connection gracefully
        webSocket.close(1000, null)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.i("WSCONN","SEVERED WEBSOCKET CONNECTION: Code=$code, Reason=$reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e("WSCONN","WEBSOCKET ERROR: ${t.message}")
        t.printStackTrace()
    }
}