package com.mgalal.freenowtask.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class VehicleClusterItem(private val vehicle: Vehicle) : ClusterItem {
    override fun getPosition(): LatLng =
        with(vehicle.coordinate) {
            LatLng(latitude, longitude)
        }

    override fun getTitle(): String? {
        return "Vehicle: ${vehicle.id}"
    }

    override fun getSnippet(): String? {
        return "Vehicle: ${vehicle.id}"
    }
}
