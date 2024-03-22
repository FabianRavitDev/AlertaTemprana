package com.ravit.alertatemprana.ui.view.chat

import android.app.Application
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import com.ravit.alertatemprana.ui.navigation.NavigationEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _textState = MutableStateFlow(TextFieldValue(""))
    val textState: StateFlow<TextFieldValue> = _textState.asStateFlow()

    private val _messages = MutableStateFlow<List<String>>(emptyList())
    val messages: StateFlow<List<String>> = _messages.asStateFlow()

    fun goBack() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.Back)
        }
    }
    fun addMessage(message: String) {
        _messages.value = _messages.value + message
    }

    fun onMessageSend() {
        val currentText = _textState.value.text
        if (currentText.isNotEmpty()) {
            _messages.value = _messages.value + currentText
            _textState.value = TextFieldValue("")
        }
    }

    fun updateTextState(newText: String) {
        _textState.value = TextFieldValue(newText)
    }
}