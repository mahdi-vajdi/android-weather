package com.mahdivajdi.modernweather.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mahdivajdi.modernweather.domain.DailyForecastDomainModel

private const val TABLE_NAME = "daily_forecast"

@Dao
interface DailyForecastDao {

    @Query("SELECT * FROM $TABLE_NAME WHERE city_id = :id")
    fun getDailyForecasts(id: Int): LiveData<List<DailyForecastLocalModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyForecast(dailyForecastLocalModelList: List<DailyForecastLocalModel>)

    @Update
    suspend fun updateDailyForecast(dailyForecastLocalModel: DailyForecastLocalModel)

    @Query("DELETE FROM $TABLE_NAME WHERE city_id = :cityId")
    suspend fun deleteDailyForecast(cityId: Int)
}


@Entity(tableName = TABLE_NAME)
data class DailyForecastLocalModel(
    @PrimaryKey(autoGenerate = true)
    val rowId: Int = 0,
    @ColumnInfo(name = "city_id")
    val cityId: Int,
    @ColumnInfo(name = "date")
    val date: Long,
    @ColumnInfo(name = "min_temp")
    val minTemp: Double,
    @ColumnInfo(name = "max_temp")
    val maxTemp: Double,
    @ColumnInfo(name = "icon")
    val icon: String,
    @ColumnInfo(name = "detail")
    val detail: String,
)



fun DailyForecastLocalModel.asDomainModel() =
    DailyForecastDomainModel(
        cityId = this.cityId,
        date = this.date,
        minTemp = this.minTemp.toInt(),
        maxTemp = this.maxTemp.toInt(),
        icon = this.icon,
        detail = this.detail
    )


