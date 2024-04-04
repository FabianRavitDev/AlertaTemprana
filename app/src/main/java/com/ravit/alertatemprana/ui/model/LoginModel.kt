package com.ravit.alertatemprana.ui.model

import com.google.gson.annotations.SerializedName

class User(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("id") val id: Int? = null
)

data class LoginRequest(
    @SerializedName("user") val user: User,
    @SerializedName("error") val error: String? = null
)

data class LoginResponse(
    val token: String?,
    val user: User?,
    val error: String?
)