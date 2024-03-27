package com.ravit.alertatemprana.ui.navigation

sealed class NavigationEvent {
    object NavigateToChat : NavigationEvent()
    object GOBackToStop : NavigationEvent()
}