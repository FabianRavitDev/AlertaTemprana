package com.ravit.alertatemprana.ui.view.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ravit.alertatemprana.ui.navigation.NavigationEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()



    fun goBack() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.Back)
        }
    }
}