package com.mahdivajdi.modernweather.data.repository

import com.mahdivajdi.modernweather.data.local.CityDao
import com.mahdivajdi.modernweather.data.local.cityAsDomainModel
import com.mahdivajdi.modernweather.data.remote.CityRemoteDataSource
import com.mahdivajdi.modernweather.data.remote.asDomainModel
import com.mahdivajdi.modernweather.domain.CityDomainModel
import com.mahdivajdi.modernweather.domain.toLocalModel
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

    // Get data for the city using the coordinates from location provider
    // Only used for current location feature. change if it needs to be used elsewhere
    suspend fun getRemoteCityByCoordinates(lat: Double, lon: Double, cityId: Int = 0): CityDomainModel? {
        val cityRemote = cityRemoteSource.getCityByCoordinates(lat, lon)
        cityRemote?.let { city ->
            return city.asDomainModel(cityId)
            }
        return null
    }

    fun getLocalCities(): Flow<List<CityDomainModel>> =
        cityLocalSource.getCities().map { cityLocalList ->
            cityLocalList.map {
                it.cityAsDomainModel()
            }
        }

    suspend fun deleteCity(city: CityDomainModel) {
        cityLocalSource.deleteCity(city.toLocalModel())
    }

    suspend fun insertCity(city: CityDomainModel) = cityLocalSource.insertCity(city.toLocalModel())

}



