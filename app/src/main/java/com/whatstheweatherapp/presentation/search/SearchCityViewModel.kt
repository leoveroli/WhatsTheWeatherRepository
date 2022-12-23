package com.whatstheweatherapp.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whatstheweatherapp.domain.DispatcherProvider
import com.whatstheweatherapp.common.Result
import com.whatstheweatherapp.common.ViewModelEvent
import com.whatstheweatherapp.domain.models.City
import com.whatstheweatherapp.domain.usecase.CitiesListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class SearchCityViewModel @Inject constructor(
    private val mDispatcherProvider: DispatcherProvider,
    private val mCitiesListUseCase: CitiesListUseCase
) : ViewModel() {

    private val mCitiesListLiveData: MutableLiveData<ViewModelEvent<ArrayList<City>>> =
        MutableLiveData()
    val citiesListObserver: LiveData<ViewModelEvent<ArrayList<City>>> = mCitiesListLiveData

    fun retrieveCitiesList(query: String, appId: String) {
        mCitiesListLiveData.value = ViewModelEvent.Loading

        CoroutineScope(mDispatcherProvider.main()).launch {
            when (val result = mCitiesListUseCase.retrieveCitiesList(query, appId)) {
                is Result.Success -> {
                    mCitiesListLiveData.value = ViewModelEvent.Done(result.data)
                }
                is Result.Failure -> {
                    mCitiesListLiveData.value = ViewModelEvent.Error(result.e)
                }
            }
        }
    }
}