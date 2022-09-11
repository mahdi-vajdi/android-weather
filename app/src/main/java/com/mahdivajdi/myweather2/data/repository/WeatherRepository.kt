package com.mahdivajdi.myweather2.data.repository

import android.util.Log
import androidx.lifecycle.*
import com.mahdivajdi.myweather2.data.local.*
import com.mahdivajdi.myweather2.data.remote.*
import com.mahdivajdi.myweather2.domain.CurrentWeatherDomainModel
import com.mahdivajdi.myweather2.domain.DailyForecastDomainModel
import com.mahdivajdi.myweather2.domain.HourlyForecastDomainModel

class WeatherRepository(
    private val weatherRemoteSource: WeatherRemoteDataSource,
    private val currentLocalSource: CurrentWeatherDao,
    private val dailyLocalSource: DailyForecastDao,
    private val hourlyLocalSource: HourlyForecastDao,
) {

    companion object {
        private const val TAG = "weatherApi"
    }

    // The CurrentWeather that we get from api
    fun currentWeather(cityId: Int): LiveData<CurrentWeatherDomainModel> =
        currentLocalSource.getCurrentWeather(cityId).map {
            it.asDomainModel()
        }

    // The HourlyForecast that we get from api
    fun hourlyForecast(cityId: Int): LiveData<List<HourlyForecastDomainModel>> =
        hourlyLocalSource.getHourlyForecasts(cityId).map { hourlyList ->
            hourlyList.map { it.asDomainModel() }
        }

    // The DailyForecast that we get from api
    fun dailyForecast(cityId: Int): LiveData<List<DailyForecastDomainModel>> =
        dailyLocalSource.getDailyForecasts(cityId).map { dailyList ->
            dailyList.map { it.asDomainModel() }
        }

    suspend fun refreshWeather(cityId: Int, lat: Double, lon: Double) {
        when (val remoteWeather = weatherRemoteSource.getWeather(lat, lon)) {
            is ResultData.Success -> {
                Log.d(TAG, "weatherRepo: refreshWeather: result success")
                val remoteCurrentAsLocal =
                    remoteWeather.value.currentWeatherRemoteModel.asCurrentWeatherLocalModel(cityId)
                val remoteHourlyAsLocal =
                    remoteWeather.value.hourly!!.map {
                        it.asHourlyWeatherLocalModel(cityId)
                    }
                val remoteDailyAsLocal =
                    remoteWeather.value.daily!!.map { it.asDailyForecastLocalModel(cityId) }

                currentLocalSource.insertCurrentWeather(remoteCurrentAsLocal)

                hourlyLocalSource.deleteHourlyForecast(cityId)
                hourlyLocalSource.insertHourlyForecast(remoteHourlyAsLocal)

                dailyLocalSource.deleteDailyForecast(cityId)
                dailyLocalSource.insertDailyForecast(remoteDailyAsLocal)
            }
            is ResultData.Failure -> {
                Log.d(TAG, "getCurrentWeather: weatherRepo: ResultDataFailure")
            }
        }
    }

}