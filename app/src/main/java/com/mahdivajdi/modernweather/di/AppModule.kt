package com.mahdivajdi.modernweather.di

import com.mahdivajdi.modernweather.data.local.CityDao
import com.mahdivajdi.modernweather.data.remote.CityRemoteDataSource
import com.mahdivajdi.modernweather.data.repository.CityRepository
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
        cityDao: CityDao
    ): CityRepository {
        return CityRepository(
            cityRemoteDataSource,
            cityDao
        )
    }

}