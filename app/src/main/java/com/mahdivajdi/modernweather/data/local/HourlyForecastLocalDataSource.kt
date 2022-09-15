package com.mahdivajdi.modernweather.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mahdivajdi.modernweather.domain.HourlyForecastDomainModel

private const val TABLE_NAME = "hourly_forecast"

@Dao
interface HourlyForecastDao {

    @Query("SELECT * FROM $TABLE_NAME WHERE city_id = :id")
    fun getHourlyForecasts(id: Int): LiveData<List<HourlyForecastLocalModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyForecast(hourlyForecastLocalModelList: List<HourlyForecastLocalModel>)

    @Update
    suspend fun updateHourlyForecast(hourlyForecastLocalModel: HourlyForecastLocalModel)

    @Query("DELETE FROM $TABLE_NAME WHERE city_id = :cityId")
    suspend fun deleteHourlyForecast(cityId: Int)
}


@Entity(tableName = TABLE_NAME)
data class HourlyForecastLocalModel(
    @PrimaryKey(autoGenerate = true)
    val rowId: Int = 0,
    @ColumnInfo(name = "city_id")
    val cityId: Int,
    @ColumnInfo(name = "date")
    val date: Long,
    @ColumnInfo(name = "temp")
    val temp: Double,
    @ColumnInfo(name = "icon")
    val icon: String
)

fun HourlyForecastLocalModel?.hourlyAsDomainModel(): HourlyForecastDomainModel {
    return if (this != null) {
        HourlyForecastDomainModel(
            cityId = this.cityId,
            date = this.date,
            temp = this.temp.toInt(),
            icon = this.icon
        )
    } else {
        HourlyForecastDomainModel(
            cityId = 0,
            date = 0,
            temp = -500,
            icon = "01d",
        )
    }

}