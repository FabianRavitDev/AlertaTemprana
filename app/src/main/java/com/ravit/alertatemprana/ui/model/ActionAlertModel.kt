package com.ravit.alertatemprana.ui.model

class ActionAlertModel(
    code: String,
    action: Action
)

enum class Action {
    Start,
    Stop,
    Message
}
