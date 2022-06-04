package com.mgalal.freenowtask.di

import com.google.gson.Gson
import com.mgalal.freenowtask.data.VehicleService
import com.mgalal.freenowtask.domain.AbstractUseCase
import com.mgalal.freenowtask.domain.LoadVehiclesUseCase
import com.mgalal.freenowtask.model.Region
import com.mgalal.freenowtask.model.Vehicles
import com.mgalal.freenowtask.repositories.IVehicleRepository
import com.mgalal.freenowtask.repositories.VehicleRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Modules {
    @Provides
    @Singleton
    fun provideVehiclesService(): VehicleService {
        val builder = Retrofit.Builder()
            .baseUrl("https://fake-poi-api.mytaxi.com")
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
        return builder.create(VehicleService::class.java)
    }

    @IoDispatcher
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideLoadVehiclesUseCase( @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
                                   vehicleRepository: IVehicleRepository): AbstractUseCase< Region, Vehicles> =
        LoadVehiclesUseCase(coroutineDispatcher,vehicleRepository)
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideVehicleRepository(
        service: VehicleService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): IVehicleRepository {
        return VehicleRepositoryImpl(service, ioDispatcher)
    }
}