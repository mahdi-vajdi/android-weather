package com.mahdivajdi.myweather2.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.mahdivajdi.myweather2.data.repository.WeatherRepository
import com.mahdivajdi.myweather2.domain.CityDomainModel
import com.mahdivajdi.myweather2.domain.CurrentWeatherDomainModel
import com.mahdivajdi.myweather2.domain.DailyForecastDomainModel
import com.mahdivajdi.myweather2.domain.HourlyForecastDomainModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*


class WeatherViewModel(private val weatherRepo: WeatherRepository) : ViewModel() {

    // The Weather data that we get from data base
    lateinit var currentWeather: LiveData<CurrentWeatherDomainModel>
    lateinit var hourlyForecast: LiveData<List<HourlyForecastDomainModel>>
    lateinit var dailyForecast: LiveData<List<DailyForecastDomainModel>>

    fun init(city: CityDomainModel) {
        Log.d(TAG, "init: weatherViewModel")
        currentWeather = weatherRepo.currentWeather(city.cityId)
        hourlyForecast = weatherRepo.hourlyForecast(city.cityId)
        dailyForecast = weatherRepo.dailyForecast(city.cityId)
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