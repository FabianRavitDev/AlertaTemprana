package com.ravit.alertatemprana.ui.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Location (
    @SerializedName("location") val location: PositionModel
)
data class PositionModel (
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("id")val id: Int? = null,
    @SerializedName("user_id")val user_id: Int? = null,
    @SerializedName("created_at")val created_at: Date? = null,
    @SerializedName("updated_at")val updated_at: Date? = null,
    @SerializedName("error")val error: String? = null
)