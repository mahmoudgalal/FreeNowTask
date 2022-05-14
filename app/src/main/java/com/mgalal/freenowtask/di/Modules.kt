package com.mgalal.freenowtask.di

import com.google.gson.Gson
import com.mgalal.freenowtask.data.VehicleService
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

    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideVehicleRepository(
        service: VehicleService,
        ioDispatcher: CoroutineDispatcher
    ): IVehicleRepository {
        return VehicleRepositoryImpl(service, ioDispatcher)
    }
}