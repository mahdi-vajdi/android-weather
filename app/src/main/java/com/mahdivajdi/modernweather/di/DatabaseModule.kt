package com.mahdivajdi.modernweather.di

import android.content.Context
import androidx.room.Room
import com.mahdivajdi.modernweather.data.local.AppDatabase
import com.mahdivajdi.modernweather.data.local.CityDao
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


}