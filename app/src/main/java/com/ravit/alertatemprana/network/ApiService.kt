package com.ravit.alertatemprana.network

import com.ravit.alertatemprana.ui.model.Location
import com.ravit.alertatemprana.ui.model.LocationModel
import com.ravit.alertatemprana.ui.model.LoginRequest
import com.ravit.alertatemprana.ui.model.LoginResponse
import com.ravit.alertatemprana.ui.model.PositionModel
import com.ravit.alertatemprana.ui.model.Service
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST(NetworkManager.LOGIN)
    fun login(@Body user: LoginRequest): Call<LoginResponse>
    @POST(NetworkManager.ALERTS)
    fun sendAlert(@Header("Authorization") authToken: String, @Body service: Service): Call<LocationModel>
    @POST(NetworkManager.STOP)
    fun stopAlert(@Path("id") id: Int, @Header("Authorization") authToken: String): Call<Void>
    @POST(NetworkManager.POSITION)
    fun sendPosition(@Path("id") id: Int, @Header("Authorization") authToken: String, @Body alertRequest: Location): Call<PositionModel>
}
