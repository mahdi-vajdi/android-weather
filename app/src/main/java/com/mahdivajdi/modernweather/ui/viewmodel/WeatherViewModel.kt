package com.mahdivajdi.modernweather.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mahdivajdi.modernweather.data.repository.WeatherRepository
import com.mahdivajdi.modernweather.domain.CityDomainModel
import com.mahdivajdi.modernweather.domain.CurrentWeatherDomainModel
import com.mahdivajdi.modernweather.domain.DailyForecastDomainModel
import com.mahdivajdi.modernweather.domain.HourlyForecastDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
) : ViewModel() {

    // The Weather data that we get from data base
    lateinit var currentWeather: LiveData<CurrentWeatherDomainModel>
    lateinit var hourlyForecast: LiveData<List<HourlyForecastDomainModel>>
    lateinit var dailyForecast: LiveData<List<DailyForecastDomainModel>>

    fun init(city: CityDomainModel) {
        Log.d(TAG, "init: weatherViewModel")
        currentWeather = weatherRepository.currentWeather(city.cityId)
        hourlyForecast = weatherRepository.hourlyForecast(city.cityId)
        dailyForecast = weatherRepository.dailyForecast(city.cityId)
    }

    fun getDayOfWeek(epochTime: Long): String {
        val date = LocalDateTime.ofInstant(Instant.ofEpochSecond(epochTime), ZoneId.systemDefault())
        return date.dayOfWeek.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
    }

    companion object {
        private const val TAG = "weatherApi"
    }
}

class WeatherViewModelFactory(private val weatherRepo: WeatherRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(weatherRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}