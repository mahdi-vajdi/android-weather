package com.mahdivajdi.myweather2.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mahdivajdi.myweather2.App
import com.mahdivajdi.myweather2.data.remote.CityRemoteDataSource
import com.mahdivajdi.myweather2.data.remote.GeocodeApi
import com.mahdivajdi.myweather2.data.remote.OneCallApi
import com.mahdivajdi.myweather2.data.remote.WeatherRemoteDataSource
import com.mahdivajdi.myweather2.data.repository.CityRepository
import com.mahdivajdi.myweather2.data.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class RefreshAllWeatherWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    private val app = context as App

    private val cityRepo = CityRepository(
        CityRemoteDataSource(GeocodeApi),
        app.database.cityDao()
    )

    private val weatherRepo = WeatherRepository(
        WeatherRemoteDataSource(OneCallApi),
        app.database.currentWeatherDao(),
        app.database.dailyForecastDao(),
        app.database.hourlyForecastDao()
    )

    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            Log.d(TAG, "refresh all: doWork: worker started")

            val cityList = cityRepo.getLocalCities().first()
            for (city in cityList) {
                weatherRepo.refreshWeather(city.cityId, city.latitude, city.longitude)
            }

            Log.d(TAG, "refresh all: doWork: worker success")
        }
        return Result.success()
    }

    companion object {
        private const val TAG = "weatherWorker"
    }

}