package com.mgalal.freenowtask.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.mgalal.freenowtask.R
import com.mgalal.freenowtask.model.Vehicle
import com.mgalal.freenowtask.ui.MapsActivity
import com.mgalal.freenowtask.utils.SELECTED_VEHICLE_KEY

class VehiclesAdapter : RecyclerView.Adapter<VehiclesAdapter.ViewHolder>() {
    val items: MutableList<Vehicle> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.vehicle_list_item, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
        val idTxt: AppCompatTextView = root.findViewById(R.id.vehicle_id)
        val typeTxt: AppCompatTextView = root.findViewById(R.id.vehicle_type)
        val typeIcon: AppCompatImageView = root.findViewById(R.id.type_icon)
        private val mapIcon: AppCompatImageView = root.findViewById(R.id.go_to_map)

        fun bind(itemData: Vehicle) {
            with(itemView.context.resources) {
                idTxt.text = getString(R.string.vehicle_id, itemData.id)
                typeTxt.text = getString(R.string.vehicle_type, itemData.fleetType)

            }
            when (itemData.fleetType) {
                POOLING -> typeIcon.setImageResource(R.drawable.car1)
                TAXI -> typeIcon.setImageResource(R.drawable.taxi)
            }
            mapIcon.setOnClickListener {
                with(itemView.context) {
                    val intent = Intent(this, MapsActivity::class.java)
                    intent.putExtra(SELECTED_VEHICLE_KEY, itemData)
                    startActivity(intent)
                }
            }
        }
    }

    private companion object {
        const val POOLING = "POOLING"
        const val TAXI = "TAXI"
    }
}