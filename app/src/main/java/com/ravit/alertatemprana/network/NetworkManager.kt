package com.ravit.alertatemprana.network

import android.util.Log
import com.ravit.alertatemprana.ui.model.LocationModel
import com.ravit.alertatemprana.ui.model.MessageModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private const val MAIN_API = "https://6602d8f39d7276a7555464ff.mockapi.io/api/service/"
    const val ALERT = "alert/"
    const val MESSAGE= "menssage/"

    private fun retrofitService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(MAIN_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    fun sendLocation(locationModel: LocationModel, onSuccess: () -> Unit, onFailure: (String?) -> Unit) {
        val call = retrofitService().sendLocation(locationModel)
        call.enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("NetworkManager", "Respuesta exitosa")
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("NetworkManager", "Respuesta no exitosa: ${response.errorBody()?.string()}")
                    onFailure(errorBody)
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("NetworkManager", "Error en la red: ${t.message}")
                onFailure(t.message)
            }
        })
    }

    fun getMessage(onSuccess: (MessageModel) -> Unit, onFailure: (String?) -> Unit) {
        val call = retrofitService().getMessage()
        call.enqueue(object : retrofit2.Callback<MessageModel> {
            override fun onResponse(call: Call<MessageModel>, response: retrofit2.Response<MessageModel>) {
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        Log.d("NetworkManager", "Respuesta exitosa")
                        onSuccess(body)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("NetworkManager", "Respuesta no exitosa: $errorBody")
                    onFailure(errorBody)
                }
            }
            override fun onFailure(call: Call<MessageModel>, t: Throwable) {
                Log.d("NetworkManager", "Error en la red: ${t.message}")
                onFailure(t.message)
            }
        })
    }
}
