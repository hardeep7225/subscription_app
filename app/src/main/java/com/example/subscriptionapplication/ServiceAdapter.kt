package com.example.subscriptionapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.subscriptionapplication.CallbackServiceSelected
import com.example.subscriptionapplication.Service
import com.example.subscriptionapplication.databinding.ItemServiceBinding
import java.util.*

class ServiceAdapter(
    private val context: Context,
    services: List<Service>,
    private val listener: CallbackServiceSelected
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    private val allServices = ArrayList(services)
    private val filteredServices = ArrayList(services)

    inner class ServiceViewHolder(val binding: ItemServiceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = ItemServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = filteredServices[position]
        with(holder.binding) {
            tvItemServiceName.text = service.name

            val resId = context.resources.getIdentifier(service.image, "drawable", context.packageName)
            ivItemServeImage.setImageResource(resId)

            // Show selection icon
            ivItemServeSelected.visibility = if (service.selected) View.VISIBLE else View.GONE

            root.setOnClickListener {
                listener.onServiceSelected(service)
            }
        }
    }

    override fun getItemCount(): Int = filteredServices.size

    fun filter(query: String) {
        val lowerCaseQuery = query.lowercase(Locale.getDefault())
        filteredServices.clear()

        if (lowerCaseQuery.isEmpty()) {
            filteredServices.addAll(allServices)
        } else {
            for (service in allServices) {
                if (service.name.lowercase(Locale.getDefault()).contains(lowerCaseQuery)) {
                    filteredServices.add(service)
                }
            }
        }
        notifyDataSetChanged()
    }
}
