package com.mgalal.freenowtask.data

import com.mgalal.freenowtask.model.NetworkResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VehicleService {
    @GET(value = "/")
    suspend fun getAllVehicles(
        @Query("p1Lat") latitude1: Double,
        @Query("p1Lon") longitude1: Double,
        @Query("p2Lat") latitude2: Double,
        @Query("p2Lon") longitude2: Double
    ): Response<NetworkResponse>

}