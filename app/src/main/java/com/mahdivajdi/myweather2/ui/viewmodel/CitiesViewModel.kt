package com.mahdivajdi.myweather2.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.mahdivajdi.myweather2.data.repository.CityRepository
import com.mahdivajdi.myweather2.domain.CityDomainModel
import com.mahdivajdi.myweather2.workers.RefreshCityWeatherWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CitiesViewModel(
    application: Application,
    private val cityRepo: CityRepository,
) : ViewModel() {

    // List of the cities that we get from api
    private val _remoteCityList = MutableLiveData<List<CityDomainModel>>()
    val remoteCityList: LiveData<List<CityDomainModel>> get() = _remoteCityList

    // City based on current location data
    private val _currentCity = MutableLiveData<CityDomainModel>()
    val currentCity: LiveData<CityDomainModel> get() = _currentCity

    // List of the cities that are stored in local database
    private val _localCityList: Flow<List<CityDomainModel>> = cityRepo.getLocalCities()
    val localCityList: LiveData<List<CityDomainModel>> get() = _localCityList.asLiveData()

    private val workManager = WorkManager.getInstance(application)

    fun getRemoteCityByName(cityName: String) {
        viewModelScope.launch {
            val resultList = cityRepo.getRemoteCityByName(cityName)
            resultList?.let { results ->
                _remoteCityList.value = results.filter { it.isCity }
            }
        }
    }

    fun getRemoteCityByCoordinates(lat: Double, lon: Double) {
        viewModelScope.launch {
            val resultCity = cityRepo.getRemoteCityByCoordinates(lat, lon)
            resultCity?.let { city ->
                _currentCity.value = city
            }
        }
    }

    fun insertNewCity(city: CityDomainModel) {
        viewModelScope.launch {
            val cityId: Long = cityRepo.insertCity(city)

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
            cityRepo.deleteCity(city)
        }
    }

}

class CitiesViewModelFactory(
    private val application: Application,
    private val cityRepo: CityRepository,
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CitiesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CitiesViewModel(application, cityRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}