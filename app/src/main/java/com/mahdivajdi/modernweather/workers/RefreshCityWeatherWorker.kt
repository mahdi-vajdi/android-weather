package com.mahdivajdi.modernweather.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mahdivajdi.modernweather.App
import com.mahdivajdi.modernweather.data.remote.OneCallApi
import com.mahdivajdi.modernweather.data.remote.WeatherRemoteDataSource
import com.mahdivajdi.modernweather.data.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class RefreshCityWeatherWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    private val app = context as App

    private val weatherRepo = WeatherRepository(
        WeatherRemoteDataSource(OneCallApi),
        app.database.currentWeatherDao(),
        app.database.dailyForecastDao(),
        app.database.hourlyForecastDao()
    )

    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            Log.d(TAG, "doWork: worker started")

            val cityId = inputData.getInt("city_id", 0)
            val latitude = inputData.getDouble("city_lat", 0.0)
            val longitude = inputData.getDouble("city_lon", 0.0)

            weatherRepo.refreshWeather(cityId, latitude, longitude)

            Log.d(TAG, "doWork: worker success")
        }
        return Result.success()
    }

    companion object {
        private const val TAG = "weatherWorker"
    }

}