package com.ravit.alertatemprana.ui.model.Singleton

object UserManager {
    private var authToken: String? = null
    private var id_User: Int? = null
    private var id_message: Int? =  null

    fun setUser(token: String?, id: Int?) {
        authToken = token
        id_User = id
    }

    fun setIdMessage( id: Int?) {
        id_message = id
    }

    fun getToken(): String? {
        return authToken
    }

    fun getId(): Int? {
        return id_User
    }
    fun getIdMessage():Int? {
        return id_message
    }
}