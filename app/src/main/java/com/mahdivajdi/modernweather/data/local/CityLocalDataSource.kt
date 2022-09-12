package com.mahdivajdi.modernweather.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mahdivajdi.modernweather.domain.CityDomainModel
import kotlinx.coroutines.flow.Flow

private const val TABLE_NAME = "city_table"


@Entity(tableName = TABLE_NAME)
data class CityLocalModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "city_id")
    val cityId: Int = 0,
    @ColumnInfo(name = "name")
    val cityName: String,
    @ColumnInfo(name = "county")
    val county: String,
    @ColumnInfo(name = "state")
    val state: String,
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double
    )


@Dao
interface CityDao {

    @Query("SELECT * FROM $TABLE_NAME")
    fun getCities(): Flow<List<CityLocalModel>>

    @Query("SELECT * FROM $TABLE_NAME WHERE city_id = :id")
    fun getCity(id: Int): LiveData<CityLocalModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCity(cityLocalModel: CityLocalModel): Long

    @Update
    suspend fun updateCity(cityLocalModel: CityLocalModel)

    @Delete
    suspend fun deleteCity(cityLocalModel: CityLocalModel)
}


fun CityLocalModel.asDomainModel() =
    CityDomainModel(
        cityId = this.cityId,
        cityName = this.cityName,
        county = this.county,
        state = this.state,
        country = this.country,
        isCity = true,
        latitude = this.latitude,
        longitude = this.longitude
    )










