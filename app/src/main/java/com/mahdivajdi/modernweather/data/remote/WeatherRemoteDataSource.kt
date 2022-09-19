package com.mahdivajdi.modernweather.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
private const val API_KEY = "f20dcaf2f3af9d8de3f394ebbc680a09"


interface OneCallApiService {

    @GET("onecall?appid=$API_KEY&exclude=minutely")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ): Response<OneCallApiResponseModel>
}


class WeatherRemoteDataSource(private val api: OneCallApiService) : BaseRemoteDataSource() {

    suspend fun getWeather(lat: Double, lon: Double): ResultData<OneCallApiResponseModel> {
        return super.getData {
            api.getWeather(lat, lon)
        }
    }
}
