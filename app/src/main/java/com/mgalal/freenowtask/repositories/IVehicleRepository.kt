package com.mgalal.freenowtask.repositories

import com.mgalal.freenowtask.data.Result
import com.mgalal.freenowtask.model.NetworkResponse

interface IVehicleRepository {

    suspend fun getAllVehicles(
        latitude1: Double,
        longitude1: Double,
        latitude2: Double,
        longitude2: Double
    ): Result<NetworkResponse>

}