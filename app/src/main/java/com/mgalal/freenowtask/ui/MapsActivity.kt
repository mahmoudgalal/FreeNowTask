package com.mgalal.freenowtask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.coroutineScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.cameraIdleEvents
import com.mgalal.freenowtask.R
import com.mgalal.freenowtask.databinding.ActivityMapsBinding
import com.mgalal.freenowtask.model.Region
import com.mgalal.freenowtask.model.Vehicle
import com.mgalal.freenowtask.model.VehicleClusterItem
import com.mgalal.freenowtask.utils.SELECTED_VEHICLE_KEY
import com.mgalal.freenowtask.viewmodels.VehiclesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapsBinding
    private var selectedVehicle: Vehicle? = null
    private lateinit var markerManager: MarkerManager
    private val vehiclesViewModel by viewModels<VehiclesViewModel>()
    private lateinit var clusterManager: ClusterManager<VehicleClusterItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        selectedVehicle = intent.getParcelableExtra(SELECTED_VEHICLE_KEY)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        lifecycle.coroutineScope.launchWhenCreated {
            val googleMap = mapFragment.awaitMap()
            markerManager = MarkerManager(googleMap)
            clusterManager = ClusterManager<VehicleClusterItem>(
                this@MapsActivity,
                googleMap,
                markerManager
            )

            val selected = selectedVehicle?.coordinate?.let {
                LatLng(it.latitude, it.longitude)
            } ?: sydney
            val selectedTitle = selectedVehicle?.let {
                "Vehicle: ${it.id}"
            } ?: SYDNEY_TITLE
            googleMap.addMarker {
                position(selected)
                title(selectedTitle)
                snippet(selectedTitle)
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            }
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selected, 6f))
            launch {
                googleMap.cameraIdleEvents().collect {
                    Log.d(TAG, "Camera Idle...")
                    // load all vehicles in the camera bounds and add markers
                    addVehiclesCluster(googleMap)
                }
            }
        }
    }

    private suspend fun addVehiclesCluster(map: GoogleMap) {
        clusterManager.onCameraIdle()
        val region = map.projection.visibleRegion.latLngBounds.toRegion()
        vehiclesViewModel.loadAllVehiclesInRegion(region).join()
        val items = vehiclesViewModel.allVehicles.value?.map {
            VehicleClusterItem(it)
        }
        items?.let {
            Log.d(TAG, "Adding ${it.size} marker to the cluster")
            clusterManager.addItems(it)
            clusterManager.cluster()
        }
    }

    private companion object {
        const val TAG = "MapsActivity"
        val sydney = LatLng(-34.0, 151.0)
        const val SYDNEY_TITLE = "Default in Sydney"
        fun LatLngBounds.toRegion(): Region {
            return Region(
                northeast.latitude, northeast.longitude,
                southwest.latitude, southwest.longitude
            )
        }
    }

}