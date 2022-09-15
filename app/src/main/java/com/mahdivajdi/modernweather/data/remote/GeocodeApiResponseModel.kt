package com.mahdivajdi.modernweather.data.remote


import com.mahdivajdi.modernweather.domain.CityDomainModel
import com.squareup.moshi.Json


data class GeocodeResponseModel(
    @Json(name = "options")
    val options: Options,
    @Json(name = "results")
    val results: List<ResultsItem>?,
    @Json(name = "info")
    val info: Info,
)


data class Info(
    @Json(name = "statuscode")
    val statusCode: Int = 0,
)


data class Options(
    @Json(name = "thumbMaps")
    val thumbMaps: Boolean = false,
    @Json(name = "maxResults")
    val maxResults: Int = 0,
    @Json(name = "ignoreLatLngInput")
    val ignoreLatLngInput: Boolean = false,
)


data class ResultsItem(
    @Json(name = "locations")
    val locations: List<CityRemoteModel>?,
)


data class CityRemoteModel(
    @Json(name = "adminArea6")
    val adminArea6: String = "",
    @Json(name = "adminArea5")
    val adminArea5: String = "",
    @Json(name = "adminArea4")
    val adminArea4: String = "",
    @Json(name = "adminArea3")
    val adminArea3: String = "",
    @Json(name = "adminArea1")
    val adminArea1: String = "",
    @Json(name = "geocodeQualityCode")
    val geocodeQualityCode: String = "",
    @Json(name = "geocodeQuality")
    val geocodeQuality: String = "",
    @Json(name = "linkId")
    val linkId: String = "",
    @Json(name = "type")
    val type: String = "",
    @Json(name = "mapUrl")
    val mapUrl: String = "",
    @Json(name = "latLng")
    val latLng: LatLng,
)


data class LatLng(
    @Json(name = "lat")
    val latitude: Double,
    @Json(name = "lng")
    val longitude: Double,
)


fun CityRemoteModel.asDomainModel(cityId: Int = 0) =
    CityDomainModel(
        cityId = cityId,
        cityName = this.adminArea5,
        county = this.adminArea4,
        state = this.adminArea3,
        country = this.adminArea1,
        isCity = this.geocodeQuality == "CITY",
        latitude = this.latLng.latitude,
        longitude = this.latLng.longitude,
    )










