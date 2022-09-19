package com.mahdivajdi.modernweather.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [CityLocalModel::class, CurrentWeatherLocalModel::class,
    DailyForecastLocalModel::class, HourlyForecastLocalModel::class],
    version = 1,
    exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun dailyForecastDao(): DailyForecastDao
    abstract fun hourlyForecastDao(): HourlyForecastDao

}