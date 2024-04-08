package com.ravit.alertatemprana.network

import android.util.Log
import com.ravit.alertatemprana.ui.view.chat.ChatViewModel
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketListener(private val viewModel: ChatViewModel): WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
//        subscribeToLocationsChannel()
//        Log.d("NetworkManager", "Connected socket")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        outPut("Received socket: $text")
        viewModel.handleWebSocketMessage(text)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        outPut("Error socket: ${t.message}")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
    }

    fun outPut(Text : String) {
        Log.d("NetworkManager", "$Text")
    }
}