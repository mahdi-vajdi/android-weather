package com.mahdivajdi.modernweather.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.mahdivajdi.modernweather.data.local.CurrentWeatherDao
import com.mahdivajdi.modernweather.data.local.DailyForecastDao
import com.mahdivajdi.modernweather.data.local.HourlyForecastDao
import com.mahdivajdi.modernweather.data.local.hourlyAsDomainModel
import com.mahdivajdi.modernweather.data.local.dailyAsDomainModel
import com.mahdivajdi.modernweather.data.local.currentAsDomainModel
import com.mahdivajdi.modernweather.data.remote.*
import com.mahdivajdi.modernweather.domain.*

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
    fun currentWeather(cityId: Int): LiveData<CurrentWeatherDomainModel> {
        val localCurrent = currentLocalSource.getCurrentWeather(cityId)
        return localCurrent.map {
            it.currentAsDomainModel()
        }
    }

    // The HourlyForecast that we get from api
    fun hourlyForecast(cityId: Int): LiveData<List<HourlyForecastDomainModel>> {
        val localHourly = hourlyLocalSource.getHourlyForecasts(cityId)
        return localHourly.map { hourlyList ->
            hourlyList.map { it.hourlyAsDomainModel() }
        }
    }

    // The DailyForecast that we get from api
    fun dailyForecast(cityId: Int): LiveData<List<DailyForecastDomainModel>> {
        val localDaily = dailyLocalSource.getDailyForecasts(cityId)
        return localDaily.map { dailyList ->
            dailyList.map { it.dailyAsDomainModel() }
        }
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