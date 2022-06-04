package com.mgalal.freenowtask.domain

import com.mgalal.freenowtask.model.Region
import com.mgalal.freenowtask.model.Vehicles
import com.mgalal.freenowtask.repositories.IVehicleRepository
import kotlinx.coroutines.CoroutineDispatcher
import com.mgalal.freenowtask.data.Result
import com.mgalal.freenowtask.di.IoDispatcher
import com.mgalal.freenowtask.model.NetworkResponse
import javax.inject.Inject

class LoadVehiclesUseCase @Inject constructor(
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    private val vehicleRepository: IVehicleRepository
) : @JvmSuppressWildcards AbstractUseCase<Region,Vehicles>(
    coroutineDispatcher
) {
    override suspend fun execute(param: Region): DomainResult<Vehicles> {
        return mapToDomain(
            vehicleRepository.getAllVehicles(
                param.latitude1,
                param.longitude1,
                param.latitude2,
                param.longitude2
            )
        )
    }

    companion object {
        private fun mapToDomain(input: Result<NetworkResponse>): DomainResult<Vehicles> {
            return when (input) {
                is Result.Success ->
                    DomainResult.Success(
                        input.data.poiList
                    )
                is Result.Error -> DomainResult.Error(input.exception)
            }
        }
    }
}