package com.mgalal.freenowtask.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

typealias Vehicles = List<Vehicle>

@Parcelize
data class Vehicle(
    val id: Long,
    val coordinate: Coordinate,
    val fleetType: String,
    val heading: Double
) : Parcelable {
    @Parcelize
    data class Coordinate(
        val latitude: Double,
        val longitude: Double
    ) : Parcelable
}
