package com.ravit.alertatemprana.ui.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Service (
    @SerializedName("service") val service: LocationModel
)
class LocationModel (
    @SerializedName("id") val id: Int? = null,
    @SerializedName("user_id") val user_id: Int? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("severity") val severity: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("created_at") val created_at: Date? = null,
    @SerializedName("updated_at") val updated_at: Date? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("room_id") val room_id: Int? = null
)

enum class ServiceType {
    EMERGENCY,
    STANDARD,
    EXPRESS
}

