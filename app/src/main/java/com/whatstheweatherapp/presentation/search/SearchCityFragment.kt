package com.whatstheweatherapp.presentation.search

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.whatstheweatherapp.BuildConfig
import com.whatstheweatherapp.R
import com.whatstheweatherapp.common.ViewModelEvent
import com.whatstheweatherapp.databinding.WeatherAppSearchCityLitsFragmentLayoutBinding
import com.whatstheweatherapp.domain.models.City
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class SearchCityFragment : Fragment(), SearchCityAdapter.InteractionListener {

    private var _binding: WeatherAppSearchCityLitsFragmentLayoutBinding? = null
    private val fragmentBinding: WeatherAppSearchCityLitsFragmentLayoutBinding
        get() = _binding!!

    private val mViewModel: SearchCityViewModel by viewModels()
    private val mAdapter = SearchCityAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WeatherAppSearchCityLitsFragmentLayoutBinding.inflate(inflater, container, false)
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(fragmentBinding) {

            var debounceJob: Job? = null
            val uiScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

            searchView.doAfterTextChanged {
                debounceJob?.cancel()
                if (it != null && it.isNotEmpty()) {
                    searchCancelButton.isVisible = true
                    searchCancelButton.setOnClickListener {
                        searchView.setText("")
                    }

                    debounceJob = uiScope.launch {
                        delay(500)
                        mViewModel.retrieveCitiesList(
                            searchView.text.toString(),
                            BuildConfig.APP_ID
                        )
                    }
                } else {
                    searchCancelButton.isVisible = false
                    mAdapter.clearItems()
                }
            }

            closeButton.setOnClickListener {
                val hasNavigated = findNavController().navigateUp()
                if (!hasNavigated) {
                    activity?.finish()
                }
            }

            cityRecyclerView.adapter = mAdapter
            context?.let { ctx ->
                cityRecyclerView.addItemDecoration(
                    DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL).apply {
                        ContextCompat.getDrawable(
                            ctx,
                            R.drawable.weather_app_divider_gray
                        )?.let { drawable ->
                            setDrawable(drawable)
                        }
                    }
                )
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        with(mViewModel) {
            citiesListObserver.observe(viewLifecycleOwner) { event ->
                when (event) {
                    is ViewModelEvent.Loading -> {
                        mAdapter.setShimmerItems()
                    }

                    is ViewModelEvent.Done -> {
                        mAdapter.setCitiesList(event.data)
                    }

                    is ViewModelEvent.Error -> {
                        mAdapter.clearItems()
                    }
                }
            }
        }
    }

    override fun onCitySelected(item: City) {
        hideKeyboard()

        val action = SearchCityFragmentDirections.openCityWeatherForecast(
            selectedCity = item,
            fromSearch = true
        )
        findNavController().navigate(action)
    }

    private fun hideKeyboard() {
        activity?.let {
            val imm = it.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = it.currentFocus
            if (view == null) {
                view = View(it)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}