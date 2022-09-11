package com.mahdivajdi.myweather2.data.repository

import androidx.lifecycle.map
import com.mahdivajdi.myweather2.data.local.CityDao
import com.mahdivajdi.myweather2.data.local.asDomainModel
import com.mahdivajdi.myweather2.data.remote.*
import com.mahdivajdi.myweather2.domain.CityDomainModel
import com.mahdivajdi.myweather2.domain.toLocalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CityRepository(
    private val cityRemoteSource: CityRemoteDataSource,
    private val cityLocalSource: CityDao,
) {

    suspend fun getRemoteCityByName(cityName: String): List<CityDomainModel>? {
        val cityRemoteList = cityRemoteSource.getCityByName(cityName)
        cityRemoteList?.let { cityList ->
            return cityList.map { city ->
                city.asDomainModel()
            }
        }
        return null
    }

    suspend fun getRemoteCityByCoordinates(lat: Double, lon: Double): CityDomainModel? {
        val cityRemote = cityRemoteSource.getCityByCoordinates(lat, lon)
        cityRemote?.let { city ->
            return city.asDomainModel()
            }
        return null
    }

    fun getLocalCities(): Flow<List<CityDomainModel>> =
        cityLocalSource.getCities().map { cityLocalList ->
            cityLocalList.map { it.asDomainModel() }
        }

    suspend fun deleteCity(city: CityDomainModel) {
        cityLocalSource.deleteCity(city.toLocalModel())
    }

    suspend fun insertCity(city: CityDomainModel) = cityLocalSource.insertCity(city.toLocalModel())

}



