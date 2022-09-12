package com.mahdivajdi.modernweather.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mahdivajdi.modernweather.domain.CurrentWeatherDomainModel

private const val TABLE_NAME = "current_weather"


@Entity(tableName = TABLE_NAME, indices = [Index(value = ["city_id"], unique = true)])
data class CurrentWeatherLocalModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "row_id")
    val rowId: Int = 0,
    @ColumnInfo(name = "city_id")
    val cityId: Int,
    @ColumnInfo(name = "date")
    val date: Long,
    @ColumnInfo(name = "temp")
    val temp: Double,
    @ColumnInfo(name = "detail")
    val detail: String,
)


@Dao
interface CurrentWeatherDao {

    @Query("SELECT * FROM $TABLE_NAME WHERE city_id = :id")
    fun getCurrentWeather(id: Int): LiveData<CurrentWeatherLocalModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(currentWeatherLocalModel: CurrentWeatherLocalModel)

    @Update
    suspend fun updateCurrentWeather(currentWeatherLocalModel: CurrentWeatherLocalModel)

    @Delete
    suspend fun deleteCurrentWeather(currentWeatherLocalModel: CurrentWeatherLocalModel)
}


fun CurrentWeatherLocalModel.asDomainModel() =
    CurrentWeatherDomainModel(
        cityId = this.cityId,
        temp = this.temp.toInt() ?: -200,
        detail = this.detail,
        date = this.date
    )