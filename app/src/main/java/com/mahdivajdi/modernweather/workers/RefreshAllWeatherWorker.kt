package com.mahdivajdi.modernweather.workers

import android.content.Context
import android.util.Log.d
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.mahdivajdi.modernweather.App
import com.mahdivajdi.modernweather.data.remote.OneCallApi
import com.mahdivajdi.modernweather.data.remote.WeatherRemoteDataSource
import com.mahdivajdi.modernweather.data.repository.CityRepository
import com.mahdivajdi.modernweather.data.repository.WeatherRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

@HiltWorker
class RefreshAllWeatherWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val cityRepository: CityRepository
) :
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
            d(TAG, "refresh all: doWork: worker started")

            val cityList = cityRepository.getLocalCities().first()
            d(TAG, "cityList size: ${cityList.size}")
            for (city in cityList) {
                weatherRepo.refreshWeather(city.cityId, city.latitude, city.longitude)
            }

            d(TAG, "refresh all: doWork: worker success")
        }
        return Result.success()
    }

    companion object {
        private const val TAG = "weatherWorker"
    }

}