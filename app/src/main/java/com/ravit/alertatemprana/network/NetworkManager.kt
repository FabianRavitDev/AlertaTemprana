package com.ravit.alertatemprana.network

import android.util.Log
import com.ravit.alertatemprana.ui.model.LocationModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private const val MAIN_API = "https://6602d8f39d7276a7555464ff.mockapi.io/api/service/"
    const val ALERT = "alert/"

    private fun retrofitService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(MAIN_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    fun sendLocation(locationModel: LocationModel, onSuccess: () -> Unit) {
        val call = retrofitService().sendLocation(locationModel)
        call.enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("NetworkManager", "Respuesta exitosa")
                    onSuccess()
                } else {
                    Log.d("NetworkManager", "Respuesta no exitosa: ${response.errorBody()?.string()}")
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("NetworkManager", "Error en la red: ${t.message}")
            }
        })
    }

}
