package com.ravit.alertatemprana.network.WebSocket

// En el archivo WebSocketManager.kt dentro del directorio network

import android.util.Log
import com.ravit.alertatemprana.ui.model.MessageModel
import com.ravit.alertatemprana.ui.model.Singleton.UserManager
import com.ravit.alertatemprana.ui.view.chat.ChatViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONException

import org.json.JSONObject

object WebSocketManager {
    private var webSocket: WebSocket? = null
    fun connectToChatChannel(viewModel: ChatViewModel, roomId: Int) {
        val token = UserManager.getToken()
        val url = "wss://delivery-drone-api-d38f60aa1218.herokuapp.com/cable?token=$token"
        val request = Request.Builder().url(url).build()
        val webSocketListener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                val subscribeMessage = JSONObject()
                try {
                    subscribeMessage.put("command", "subscribe")
                    subscribeMessage.put(
                        "identifier",
                        "{\"channel\":\"RoomChannel\",\"room_id\":\"$roomId\"}"
                    )
                    subscribeMessage.put(
                        "identifier",
                        "{\"channel\":\"ServicesChannel\",\"room_id\":\"$roomId\"}"
                    )
                    webSocket.send(subscribeMessage.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                outPut(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                outPut("${t.message}")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.d("WebSocketManager", "WebSocket closed with code $code: $reason")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.d("WebSocketManager", "WebSocket closing with code $code: $reason")
            }

            fun outPut(text: String) {
                try {
                    val json = JSONObject(text)
                    if (json.has("type") && json.getString("type") == "ping") {
//                        Log.d("NetworkManager", "Received ping message: $text")
                    }
                    else if (json.has("identifier") && json.has("message"))
                    {
                        val messageJson = json.getJSONObject("message")
                        val actualMessage = messageJson.getString("message")

                        if (actualMessage == "Servicio detenido") {
                            val servicioStop = messageJson.getInt("stopedServiceId")
                            val messageModel = MessageModel(
                                id = 0,
                                room_id = servicioStop,
                                user_id = 0,
                                body = actualMessage
                            )
                            viewModel.handleWebSocketMessage(messageModel)
                        }

                        val actualJson = JSONObject(actualMessage)
                        val userId = actualJson.getInt("user_id")
                        val body = actualJson.getString("body")
                        val messageId = actualJson.getInt("id")
                        val roomId = actualJson.getInt("room_id")

                        val messageModel = MessageModel(
                            id = messageId,
                            room_id = roomId,
                            user_id = userId,
                            body = body
                        )
                        Log.d("NetworkManager", "Received unknown message: $actualJson")
                        if (UserManager.getId() != userId) {
                            viewModel.handleWebSocketMessage(messageModel)
                        }
                    }
                    else {
                        Log.d("NetworkManager", "Received unknown message: $text")
                    }
                } catch (e: JSONException) {
                    Log.e("NetworkManager", "Error parsing message: $text")
                }
            }
        }
        webSocket = OkHttpClient().newWebSocket(request, webSocketListener)
    }

    fun disconnect() {
        webSocket?.cancel()
        webSocket?.close(1000, "Socket closed by client")
        webSocket = null

    }
}
