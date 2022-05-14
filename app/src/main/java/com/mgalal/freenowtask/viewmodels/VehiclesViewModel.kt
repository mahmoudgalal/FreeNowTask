package com.mgalal.freenowtask.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgalal.freenowtask.data.Result
import com.mgalal.freenowtask.model.NetworkResponse
import com.mgalal.freenowtask.model.Region
import com.mgalal.freenowtask.model.Vehicles
import com.mgalal.freenowtask.repositories.IVehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehiclesViewModel @Inject constructor(private val vehicleRepository: IVehicleRepository) :
    ViewModel() {

    val allVehicles: MutableLiveData<Vehicles> = MutableLiveData(listOf())
    val error: MutableLiveData<String> = MutableLiveData("")

    private val handler = CoroutineExceptionHandler { _, exception ->
        error.value = exception.message
    }

    fun loadAllVehiclesInRegion(region: Region): Job {
        return viewModelScope.launch(handler) {
            val vehicles = with(region) {
                vehicleRepository.getAllVehicles(
                    latitude1,
                    longitude1,
                    latitude2,
                    longitude2
                )
            }
            when (vehicles) {
                is Result.Success<NetworkResponse> ->
                    allVehicles.value = vehicles.data.poiList
                is Result.Error ->
                    error.value = vehicles.exception.message
            }
        }
    }
}