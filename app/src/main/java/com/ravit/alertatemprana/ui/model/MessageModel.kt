package com.ravit.alertatemprana.ui.model

data class MessageModel (
    val code: String,
    val sender: SenderType,
    val message: String
)

enum class SenderType {
    RESPONSE,
    SEND
}
