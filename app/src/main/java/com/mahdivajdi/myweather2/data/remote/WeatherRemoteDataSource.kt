package com.mahdivajdi.myweather2.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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

object OneCallApi {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val instance: OneCallApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        retrofit.create(OneCallApiService::class.java)
    }
}


class WeatherRemoteDataSource(private val api: OneCallApi) : BaseRemoteDataSource() {

    suspend fun getWeather(lat: Double, lon: Double): ResultData<OneCallApiResponseModel> {
        return super.getData {
            api.instance.getWeather(lat, lon)
        }
    }
}
