package com.mahdivajdi.modernweather.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mahdivajdi.modernweather.data.repository.WeatherRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class RefreshCityWeatherWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val weatherRepository: WeatherRepository,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            Log.d(TAG, "doWork: worker started")

            val cityId = inputData.getInt("city_id", 0)
            val latitude = inputData.getDouble("city_lat", 0.0)
            val longitude = inputData.getDouble("city_lon", 0.0)

            weatherRepository.refreshWeather(cityId, latitude, longitude)

            Log.d(TAG, "doWork: worker success")
        }
        return Result.success()
    }

    companion object {
        private const val TAG = "weatherWorker"
    }

}