package com.ravit.alertatemprana.network

import android.util.Log
import com.ravit.alertatemprana.ui.model.Location
import com.ravit.alertatemprana.ui.model.LocationModel
import com.ravit.alertatemprana.ui.model.LoginRequest
import com.ravit.alertatemprana.ui.model.LoginResponse
import com.ravit.alertatemprana.ui.model.PositionModel
import com.ravit.alertatemprana.ui.model.Service
import com.ravit.alertatemprana.ui.model.Singleton.UserManager
import com.ravit.alertatemprana.ui.model.User
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private const val MAIN_API = "https://delivery-drone-api-d38f60aa1218.herokuapp.com/"
    const val LOGIN = "login/"
    const val ALERTS= "services/"
    const val STOP = "services/{id}/stop/"
    const val POSITION = "services/{id}/locations/"

//    private fun provideOkHttpClient(): OkHttpClient { // ver URL
//        val loggingInterceptor = LoggingInterceptor()
//        return OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .build()
//    }

    private fun retrofitService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(MAIN_API)
//            .client(provideOkHttpClient()) // ver URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    fun loginLocation(onSuccess: () -> Unit, onFailure: (String?) -> Unit) {
        val user = User("user@example.com", "password")
        val loginRequest = LoginRequest(user)
        val call = retrofitService().login(loginRequest)
        call.enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: retrofit2.Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    val id = response.body()?.user?.id
                    Log.d("NetworkManager", "Respuesta id: $id exitosa token < ${token} >")
                    if (token != null && id != null) {
                        UserManager.setUser(token, id)
                        onSuccess()
                    } else {
                        onFailure("Token o id no válido")
                    }
                } else {
                    Log.d("NetworkManager", "Respuesta no exitosa: ${response.message()}")
                    onFailure(response.message())
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("NetworkManager", "Error en la red: ${t.message}")
                onFailure(t.message)
            }
        })
    }

    fun sendAlert(alertRequest: LocationModel, onSuccess: (LocationModel) -> Unit, onFailure: (String?) -> Unit) {
        val token = UserManager.getToken()
        if (token != null) {
            val service = Service(alertRequest)
            val call = retrofitService().sendAlert(token, service)

            call.enqueue(object : retrofit2.Callback<LocationModel> {
                override fun onResponse(call: Call<LocationModel>, response: retrofit2.Response<LocationModel>) {
                    if (response.isSuccessful) {
                        val locationModel = response.body()
                        Log.d("NetworkManager", "Alerta enviada exitosamente: id - ${locationModel?.id} ")
                        UserManager.setIdMessage(locationModel?.id)
                        if (locationModel != null) {
                            onSuccess(locationModel)
                        }
                    } else {
                        Log.d("NetworkManager", "Error al enviar first alerta: ${response.message()}")
                        onFailure(response.message())
                    }
                }

                override fun onFailure(call: Call<LocationModel>, t: Throwable) {
                    Log.d("NetworkManager", "Error en la red al enviar alerta: ${t.message}")
                    onFailure(t.message)
                }
            })
        } else {
            Log.e("NetworkManager", "Token null")
            onFailure("Token nulo")
        }
    }

    fun sendLocation(location: PositionModel, onSuccess: (PositionModel) -> Unit, onFailure: (String?) -> Unit){
        val token = UserManager.getToken()
        val id = UserManager.getIdMessage()
        if (token != null && id != null) {
            val serviceLocation = Location(location)
            val call = retrofitService().sendPosition(id,token,serviceLocation)
            call.enqueue(object : retrofit2.Callback<PositionModel> {
                override fun onResponse(call: Call<PositionModel>, response: retrofit2.Response<PositionModel>) {
                    if (response.isSuccessful) {
                        val resonse = response.body()
                        Log.d("NetworkManager", "Location enviada exitosamente: id - ${id}")
                        UserManager.setIdMessage(id)
                        if (resonse != null) {
                            onSuccess(resonse)
                        }
                    } else {
                        Log.d("NetworkManager", "Error al enviar location: ${response.message()}")
                        onFailure(response.message())
                    }
                }

                override fun onFailure(call: Call<PositionModel>, t: Throwable) {
                    Log.d("NetworkManager", "Error en la red al enviar location: ${t.message}")
                    onFailure(t.message)
                }
            })
        } else {
            Log.e("NetworkManager", "Token null")
            onFailure("Token nulo")
        }
    }

    fun stopAlert(onSuccess: () -> Unit, onFailure: (String?) -> Unit) {
        val token = UserManager.getToken()
        val idMessage = UserManager.getIdMessage()
        if (token != null && idMessage != null) {
                val call = retrofitService().stopAlert(idMessage, token)
                call.enqueue(object : retrofit2.Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                        if (response.isSuccessful) {
                            onSuccess()
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.d("NetworkManager", "Error en Stop alerta: ${response}")
                            onFailure(errorBody)
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d("NetworkManager", "Error en la red al enviar alerta: ${t.message}")
                        onFailure(t.message)
                    }
                })
        } else {
            Log.e("NetworkManager", "Token nulo")
            onFailure("Token nulo")
        }
    }
}

//class LoggingInterceptor : Interceptor { // mostrar la URL de endpoint
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val request = chain.request()
//        val url = request.url()
//        Log.e("NetworkManager", "URL: $url")
//        return chain.proceed(request)
//    }
//}