package com.whatstheweatherapp.presentation.landing.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.whatstheweatherapp.R
import com.whatstheweatherapp.common.AdapterRowWrapper
import com.whatstheweatherapp.common.BindingViewHolder
import com.whatstheweatherapp.databinding.WeatherAppDayItemBinding
import com.whatstheweatherapp.databinding.WeatherAppEmptyItemBinding

class CityWeatherDaysAdapter(
    private val interactionListener: DaysInteractionListener
) : RecyclerView.Adapter<BindingViewHolder>() {
    private val mItems = arrayListOf<AdapterRowWrapper<*>>()
    private var lastSelectedItem = 0
    private val DAY_ITEM = 0

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return mItems[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = when (viewType) {
            DAY_ITEM -> {
                WeatherAppDayItemBinding.inflate(inflater, parent, false)
            }

            else -> {
                WeatherAppEmptyItemBinding.inflate(inflater, parent, false)
            }
        }

        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        when (holder.binding) {
            is WeatherAppDayItemBinding -> {
                if (lastSelectedItem == position) {
                    holder.itemView.setBackgroundResource(R.drawable.weather_app_day_item_selected_background)
                } else {
                    holder.itemView.setBackgroundResource(R.drawable.weather_app_day_item_not_selected_background)
                }

                val day = mItems[position].data as String
                holder.binding.dayTV.text = day
                holder.itemView.setOnClickListener {
                    val oldSelected = lastSelectedItem
                    lastSelectedItem = holder.adapterPosition
                    notifyItemChanged(oldSelected)
                    holder.itemView.setBackgroundResource(R.drawable.weather_app_day_item_selected_background)

                    interactionListener.onDaySelected(day)
                }
            }
        }
    }

    fun setDays(daysList: ArrayList<String>) {
        mItems.addAll(
            daysList.map {
                AdapterRowWrapper(it, DAY_ITEM)
            }
        )

        notifyDataSetChanged()
    }

    interface DaysInteractionListener {
        fun onDaySelected(day: String)
    }
}