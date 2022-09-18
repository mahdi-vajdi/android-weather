package com.mahdivajdi.modernweather.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.mahdivajdi.modernweather.data.repository.CityRepository
import com.mahdivajdi.modernweather.domain.CityDomainModel
import com.mahdivajdi.modernweather.workers.RefreshCityWeatherWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    application: Application,
    private val cityRepository: CityRepository,
) : ViewModel() {

    // List of the cities that we get from api
    private val _remoteCityList = MutableLiveData<List<CityDomainModel>>()
    val remoteCityList: LiveData<List<CityDomainModel>> get() = _remoteCityList

    // City based on current location data
    private val _currentCity = MutableLiveData<CityDomainModel>()
    val currentCity: LiveData<CityDomainModel> get() = _currentCity

    // List of the cities that are stored in local database
    private val _localCityList: Flow<List<CityDomainModel>> = cityRepository.getLocalCities()
    val localCityList: LiveData<List<CityDomainModel>> get() = _localCityList.asLiveData()

    private val workManager = WorkManager.getInstance(application)

    fun getRemoteCityByName(cityName: String) {
        viewModelScope.launch {
            val resultList = cityRepository.getRemoteCityByName(cityName)
            resultList?.let { results ->
                _remoteCityList.value = results.filter { it.isCity }
            }
        }
    }

    // Get data for the city using the coordinates from location provider
    // Only used for current location feature. change if it needs to be used elsewhere
    fun getRemoteCityByCoordinates(lat: Double, lon: Double, cityId: Int = 0) {
        viewModelScope.launch {
            val resultCity = cityRepository.getRemoteCityByCoordinates(lat, lon, cityId)
            resultCity?.let { city ->
                _currentCity.value = city
            }
        }
    }

    fun insertNewCity(city: CityDomainModel) {
        viewModelScope.launch {
            val cityId: Long = cityRepository.insertCity(city)

            // Initiate worker to request weather data for the added city
            val workerInputData = Data.Builder()
                .putInt("city_id", cityId.toInt())
                .putDouble("city_lat", city.latitude)
                .putDouble("city_lon", city.longitude)

            val workRequest = OneTimeWorkRequestBuilder<RefreshCityWeatherWorker>()
                .setInputData(workerInputData.build())
                .build()
            workManager.enqueue(workRequest)
        }
    }

    fun deleteCity(city: CityDomainModel) {
        viewModelScope.launch {
            cityRepository.deleteCity(city)
        }
    }

}