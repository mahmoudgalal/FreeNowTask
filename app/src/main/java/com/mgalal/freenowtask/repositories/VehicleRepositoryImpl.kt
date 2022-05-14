package com.mgalal.freenowtask.repositories

import com.mgalal.freenowtask.data.Result
import com.mgalal.freenowtask.data.VehicleService
import com.mgalal.freenowtask.model.NetworkResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VehicleRepositoryImpl @Inject constructor(
    private val service: VehicleService,
    private val ioDispatcher: CoroutineDispatcher
) : IVehicleRepository {
    override suspend fun getAllVehicles(
        latitude1: Double,
        longitude1: Double,
        latitude2: Double,
        longitude2: Double
    ): Result<NetworkResponse> = withContext(ioDispatcher) {
            Result.fromResponse(
                service.getAllVehicles(
                    latitude1,
                    longitude1,
                    latitude2,
                    longitude2
                )
            )
        }
}