package com.mahdivajdi.modernweather.di

import com.mahdivajdi.modernweather.data.remote.CityRemoteDataSource
import com.mahdivajdi.modernweather.data.remote.GeocodeApiService
import com.mahdivajdi.modernweather.data.remote.OneCallApiService
import com.mahdivajdi.modernweather.data.remote.WeatherRemoteDataSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Singleton
    fun provideGeocodeApiService(): GeocodeApiService {
        val BASE_URL = "http://www.mapquestapi.com/geocoding/v1/"
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeocodeApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesCityRemoteDataSource(geocodeApi: GeocodeApiService): CityRemoteDataSource {
        return CityRemoteDataSource(geocodeApi)
    }


    @Provides
    @Singleton
    fun providesOnecallApiService(): OneCallApiService {
        val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(OneCallApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesWeatherRemoteDataSource(oneCallApi: OneCallApiService): WeatherRemoteDataSource {
        return WeatherRemoteDataSource(oneCallApi)
    }


}