package com.ravit.alertatemprana.ui.view.chat

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.ravit.alertatemprana.ui.navigation.NavigationEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.ravit.alertatemprana.network.NetworkManager
import com.ravit.alertatemprana.network.WebSocket.WebSocketRoomChannel
import com.ravit.alertatemprana.ui.model.MessageModel
import com.ravit.alertatemprana.ui.model.Singleton.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatViewModel(val room_id: Int) : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _textState = MutableStateFlow(TextFieldValue(""))
    val textState: StateFlow<TextFieldValue> = _textState.asStateFlow()

    private val _messages = MutableStateFlow<List<MessageModel>>(emptyList())
    val messages: StateFlow<List<MessageModel>> = _messages.asStateFlow()

    private val _isStopLocation = MutableStateFlow(false)
    val isStopLocation = _isStopLocation.asStateFlow()

    val userID = UserManager.getId()

    init {
        if (room_id != null) {
            WebSocketRoomChannel.connectToChatChannel(this, room_id)
            _isStopLocation.value = false
        }
    }

    fun goBack() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.GOBackToStop)
        }
    }

    fun onMessageSend() {
        val currentText = _textState.value.text.trim()
        if (currentText.isNotEmpty()) {
            val newMessage = MessageModel(body = currentText, user_id = userID)
            _messages.value = _messages.value + newMessage
            NetworkManager.sendMessage(room_id, newMessage,
                onSuccess = {
                    Log.d("NetworkManager", "send message = $it")
            },
                onFailure = {
                    Log.d("NetworkManager", "Error = $it")
                }
            )
            _textState.value = TextFieldValue("")
        }
    }
    fun handleWebSocketMessage(message: MessageModel) {
        if (message.body == "Servicio detenido") {
            _isStopLocation.value = true
            return
        }
        _messages.value = _messages.value + message
    }

    fun updateTextState(newText: String) {
        _textState.value = TextFieldValue(newText)
    }

    override fun onCleared() {
        super.onCleared()
    }
}