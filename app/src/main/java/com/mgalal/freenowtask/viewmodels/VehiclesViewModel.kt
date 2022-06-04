package com.mgalal.freenowtask.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mgalal.freenowtask.domain.AbstractUseCase
import com.mgalal.freenowtask.domain.DomainResult
import com.mgalal.freenowtask.model.Region
import com.mgalal.freenowtask.model.Vehicles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehiclesViewModel @Inject constructor(private val vehiclesUseCase: @JvmSuppressWildcards AbstractUseCase<Region, Vehicles>) :
    ViewModel() {

    val allVehicles: MutableLiveData<Vehicles> = MutableLiveData(listOf())
    val error: MutableLiveData<String> = MutableLiveData("")

    private val handler = CoroutineExceptionHandler { _, exception ->
        error.value = exception.message
    }

    fun loadAllVehiclesInRegion(region: Region): Job {
        return viewModelScope.launch(handler) {
            val vehicles = with(region) {
                vehiclesUseCase(this)
            }
            when (vehicles) {
                is DomainResult.Success ->
                    allVehicles.value = vehicles.data
                is DomainResult.Error ->
                    error.value = vehicles.exception.message
            }
        }
    }
}