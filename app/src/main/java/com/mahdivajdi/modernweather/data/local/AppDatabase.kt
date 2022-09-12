package com.mahdivajdi.modernweather.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "weather_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}