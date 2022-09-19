package com.mahdivajdi.modernweather.di

import com.mahdivajdi.modernweather.data.local.CityDao
import com.mahdivajdi.modernweather.data.local.CurrentWeatherDao
import com.mahdivajdi.modernweather.data.local.DailyForecastDao
import com.mahdivajdi.modernweather.data.local.HourlyForecastDao
import com.mahdivajdi.modernweather.data.remote.CityRemoteDataSource
import com.mahdivajdi.modernweather.data.remote.WeatherRemoteDataSource
import com.mahdivajdi.modernweather.data.repository.CityRepository
import com.mahdivajdi.modernweather.data.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesCityRepository(
        cityRemoteDataSource: CityRemoteDataSource,
        cityDao: CityDao,
    ): CityRepository {
        return CityRepository(
            cityRemoteDataSource,
            cityDao
        )
    }

    @Provides
    @Singleton
    fun providesWeatherRepository(
        weatherRemoteSource: WeatherRemoteDataSource,
        currentLocalSource: CurrentWeatherDao,
        dailyLocalSource: DailyForecastDao,
        hourlyLocalSource: HourlyForecastDao,
    ): WeatherRepository {
        return WeatherRepository(
            weatherRemoteSource,
            currentLocalSource,
            dailyLocalSource,
            hourlyLocalSource,
        )
    }

}