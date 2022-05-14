package com.mgalal.freenowtask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mgalal.freenowtask.adapters.VehiclesAdapter
import com.mgalal.freenowtask.databinding.ActivityMainBinding
import com.mgalal.freenowtask.utils.hamburgRegion
import com.mgalal.freenowtask.viewmodels.VehiclesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val vehiclesViewModel by viewModels<VehiclesViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var vehiclesAdapter: VehiclesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vehiclesAdapter = VehiclesAdapter()

        with(binding.vehiclesRecycler) {
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.VERTICAL, false
            )
            adapter = vehiclesAdapter
        }
        with(vehiclesViewModel) {
            allVehicles.observe(this@MainActivity) {
                Log.d(TAG, it.toString())
                if (it.isNotEmpty())
                    binding.loadProgress.visibility = View.INVISIBLE
                with(vehiclesAdapter) {
                    items.clear()
                    items += it
                    notifyDataSetChanged()
                }
            }
            error.observe(this@MainActivity) {
                if (it.isEmpty())
                    return@observe
                binding.loadProgress.visibility = View.INVISIBLE
                Log.e(TAG, it)
                Snackbar.make(
                    this@MainActivity,
                    binding.vehiclesRecycler,
                    "Error: $it",
                    Snackbar.LENGTH_LONG
                ).show()
            }
            loadAllVehiclesInRegion(hamburgRegion)
        }
    }

    private companion object {
        const val TAG = "MainActivity"
    }
}