package com.mgalal.freenowtask

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mgalal.freenowtask.data.VehicleService
import com.mgalal.freenowtask.domain.LoadVehiclesUseCase
import com.mgalal.freenowtask.model.NetworkResponse
import com.mgalal.freenowtask.model.Vehicle
import com.mgalal.freenowtask.repositories.IVehicleRepository
import com.mgalal.freenowtask.repositories.VehicleRepositoryImpl
import com.mgalal.freenowtask.utils.getOrAwaitValue
import com.mgalal.freenowtask.utils.hamburgRegion
import com.mgalal.freenowtask.utils.observeForTesting
import com.mgalal.freenowtask.viewmodels.VehiclesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.mock
import retrofit2.Response

@ExperimentalCoroutinesApi
class VehiclesViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var vehiclesViewModel: VehiclesViewModel
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
    private lateinit var vehicleRepository: IVehicleRepository
    private lateinit var loadVehiclesUseCase: LoadVehiclesUseCase
    private lateinit var vehicleService: VehicleService


    @Before
    fun setupViewModel() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun testLoadingAllVehiclesSuccessfully() {
        runTest {
            vehicleService = mock {
                onBlocking { getAllVehicles(any(), any(), any(), any()) } doSuspendableAnswer {
                    withContext(testDispatcher) {
                        Response.success(networkResponse)
                    }
                }

            }
            vehicleRepository = VehicleRepositoryImpl(vehicleService, testDispatcher)
            loadVehiclesUseCase = LoadVehiclesUseCase(testDispatcher, vehicleRepository)
            vehiclesViewModel = VehiclesViewModel(loadVehiclesUseCase)

            vehiclesViewModel.loadAllVehiclesInRegion(hamburgRegion)
            vehiclesViewModel.allVehicles.observeForTesting {
                // Execute pending coroutines actions
                advanceUntilIdle()
                // And data correctly loaded
                with(vehiclesViewModel.allVehicles.getOrAwaitValue()) {
                    assertThat(
                        "Not expected size",
                        size == 3
                    )
                    assertThat(
                        "Not expected list",
                        this == networkResponse.poiList
                    )
                }

            }
        }
    }

    @Test
    fun testLoadingAllVehiclesUnSuccessfully() {
        runTest {
            //Prepare
            vehicleService = mock {
                onBlocking { getAllVehicles(any(), any(), any(), any()) } doSuspendableAnswer {
                    withContext(testDispatcher) {
                        Response.error(404, ResponseBody.create(MediaType.get("text/plain"), ""))
                    }
                }
            }
            vehicleRepository = VehicleRepositoryImpl(vehicleService, testDispatcher)
            loadVehiclesUseCase = LoadVehiclesUseCase(testDispatcher, vehicleRepository)
            vehiclesViewModel = VehiclesViewModel(loadVehiclesUseCase)

            // do
            vehiclesViewModel.loadAllVehiclesInRegion(hamburgRegion)
            vehiclesViewModel.allVehicles.observeForTesting {
                // Execute pending coroutines actions
                advanceUntilIdle()
                // Assert data loading error
                assertThat(
                    "Not expected size",
                    vehiclesViewModel.allVehicles.getOrAwaitValue().isEmpty()
                )
            }
            vehiclesViewModel.error.observeForTesting {
                // Execute pending coroutines actions
                advanceUntilIdle()
                // Assert data loading error
                assertThat(
                    "Not expected error",
                    vehiclesViewModel.error.getOrAwaitValue().isNotEmpty()
                )
            }

        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private companion object {
        /**
         *Example object
         *  {
                "id": 971542,
                "coordinate": {
                "latitude": 53.40049870576813,
                "longitude": 10.051600237976745
                },
                "fleetType": "TAXI",
                "heading": 348.5883741300672
                },
                {
                "id": 365793,
                "coordinate": {
                "latitude": 53.59048260657351,
                "longitude": 10.00699641792453
                },
                "fleetType": "TAXI",
                "heading": 65.51118273038776
                },
                {
                "id": 558083,
                "coordinate": {
                "latitude": 53.4136480157143,
                "longitude": 10.082936465651974
                },
                "fleetType": "TAXI",
                "heading": 47.723116416653525
                }
            }
         */
        val networkResponse = NetworkResponse(
            listOf(
                Vehicle(
                    id = 971542,
                    coordinate = Vehicle.Coordinate(
                        latitude = 53.40049870576813,
                        longitude = 10.051600237976745
                    ),
                    fleetType = "TAXI",
                    heading = 348.5883741300672,
                ),
                Vehicle(
                    id = 365793,
                    coordinate = Vehicle.Coordinate(
                        latitude = 53.59048260657351,
                        longitude = 10.00699641792453
                    ),
                    fleetType = "POOLING",
                    heading = 65.51118273038776,
                ),
                Vehicle(
                    id = 558083,
                    coordinate = Vehicle.Coordinate(
                        latitude = 53.4136480157143,
                        longitude = 10.082936465651974
                    ),
                    fleetType = "TAXI",
                    heading = 47.723116416653525,
                )
            )
        )
    }
}