package com.whatstheweatherapp.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.whatstheweatherapp.common.AdapterRowWrapper
import com.whatstheweatherapp.common.BindingViewHolder
import com.whatstheweatherapp.databinding.WeatherAppEmptyItemBinding
import com.whatstheweatherapp.databinding.WeatherAppLabelItemBinding
import com.whatstheweatherapp.databinding.WeatherAppShimmerLabelItemBinding
import com.whatstheweatherapp.domain.models.City

class SearchCityAdapter(
    private val mInteractionListener: InteractionListener
) : RecyclerView.Adapter<BindingViewHolder>() {

    private val mItems = arrayListOf<AdapterRowWrapper<*>>()
    private val SHIMMER_ITEM = 0
    private val CITY_ITEM = 1

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return mItems[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SHIMMER_ITEM -> {
                BindingViewHolder(
                    WeatherAppShimmerLabelItemBinding.inflate(inflater, parent, false)
                )
            }

            CITY_ITEM -> {
                BindingViewHolder(
                    WeatherAppLabelItemBinding.inflate(inflater, parent, false)
                )
            }

            else -> {
                BindingViewHolder(
                    WeatherAppEmptyItemBinding.inflate(inflater, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        if (holder.binding is WeatherAppLabelItemBinding) {
            val city = mItems[position].data as City
            if (city.name != null && city.country != null) {
                var description = city.name + ", " + city.country
                if (city.state != null) {
                    description += ", " + city.state
                }

                holder.binding.cityTV.text = description
                holder.itemView.setOnClickListener {
                    mInteractionListener.onCitySelected(city)
                }
            }
        }
    }

    fun setShimmerItems() {
        mItems.clear()
        for (i in 0..2) {
            mItems.add(AdapterRowWrapper(null, SHIMMER_ITEM))
        }

        notifyDataSetChanged()
    }

    fun setCitiesList(citiesList: ArrayList<City>) {
        mItems.clear()
        mItems.addAll(
            citiesList.map {
                AdapterRowWrapper(it, CITY_ITEM)
            }
        )

        notifyDataSetChanged()
    }

    fun clearItems() {
        mItems.clear()
        notifyDataSetChanged()
    }

    interface InteractionListener {
        fun onCitySelected(item: City)
    }
}