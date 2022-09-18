package com.mahdivajdi.modernweather.data.remote

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = "http://www.mapquestapi.com/geocoding/v1/"
private const val API_KEY = "eVmcyRGkTcctlDVmtr0AqxEyoF7EMjnA"


interface GeocodeApiService {

    @GET("address?key=$API_KEY")
    suspend fun getCityByName(@Query("location") cityName: String): Response<GeocodeResponseModel>

    @GET("reverse?key=$API_KEY&maxResults=1")
    suspend fun getCityByCoordinates(@Query("location") latLon: String): Response<GeocodeResponseModel>

}


object GeocodeApi {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val instance: GeocodeApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        retrofit.create(GeocodeApiService::class.java)
    }

}


class CityRemoteDataSource(private val api: GeocodeApiService) : BaseRemoteDataSource() {

    suspend fun getCityByName(cityName: String): List<CityRemoteModel>? {
        Log.d("weatherApi", "CityRemoteDataSource: getCityByName: ")
        val result = super.getData {
            api.getCityByName(cityName)
        }
        Log.d("weatherApi", "getCityByName: $result")
        if (result is ResultData.Success) {
            Log.d("weatherApi", "getCityByName: ResultData is Success")
            return result.value.results?.get(0)?.locations
        }
        return null
    }

    suspend fun getCityByCoordinates(lat: Double, lon: Double): CityRemoteModel? {
        Log.d("weatherApi", "CityRemoteDataSource: getCityByCoordinates: ")
        val result = super.getData {
            val latLon = "$lat,$lon"
            Log.d("weatherApi", "CityRemoteDataSource: getCityByCoordinates: latLon= $latLon")
            api.getCityByCoordinates(latLon)
        }
        Log.d("weatherApi", "getCityByCoordinates: $result")
        if (result is ResultData.Success) {
            Log.d("weatherApi", "getCityByCoordinates: ResultData is Success")
            return result.value.results?.get(0)?.locations?.get(0)
        }
        return null
    }

}





