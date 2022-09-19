package com.mahdivajdi.modernweather.di

import android.content.Context
import androidx.room.Room
import com.mahdivajdi.modernweather.data.local.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun providesAppDatabases(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "weather_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providesCityDao(appDatabase: AppDatabase): CityDao {
        return appDatabase.cityDao()
    }

    @Provides
    fun providesCurrentWeatherDao(appDatabase: AppDatabase): CurrentWeatherDao {
        return appDatabase.currentWeatherDao()
    }

    @Provides
    fun providesHourlyForecastDao(appDatabase: AppDatabase): HourlyForecastDao {
        return appDatabase.hourlyForecastDao()
    }

    @Provides
    fun providesDailyForecastDao(appDatabase: AppDatabase): DailyForecastDao {
        return appDatabase.dailyForecastDao()
    }


}