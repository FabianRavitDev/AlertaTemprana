package com.ravit.alertatemprana.network

import com.ravit.alertatemprana.ui.model.LocationModel
import com.ravit.alertatemprana.ui.model.MessageModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST(NetworkManager.ALERT)
    fun sendLocation(@Body locationModel: LocationModel): Call<Void>

    @GET(NetworkManager.MESSAGE)
    fun getMessage(): Call<List<MessageModel>>
}
