package com.ravit.alertatemprana.ui.model

class LocationModel (
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val source_type: ServiceType?
)

enum class ServiceType {
    EMERGENCY,
    STANDARD,
    EXPRESS
}
