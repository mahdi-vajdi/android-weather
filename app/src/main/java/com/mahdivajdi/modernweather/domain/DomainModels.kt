package com.mahdivajdi.modernweather.domain

import android.os.Parcel
import android.os.Parcelable
import com.mahdivajdi.modernweather.data.local.CityLocalModel


data class CityDomainModel(
    val cityId: Int = 0,
    val cityName: String,
    val county: String,
    val state: String,
    val country: String,
    val isCity: Boolean = true,
    val latitude: Double,
    val longitude: Double
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<CityDomainModel> {
            override fun createFromParcel(parcel: Parcel) = CityDomainModel(parcel)
            override fun newArray(size: Int) = arrayOfNulls<CityDomainModel>(size)
        }
    }

    private constructor(parcel: Parcel) : this (
        cityId = parcel.readInt(),
        cityName = parcel.readString() ?: "No Name",
        county = parcel.readString() ?: "No County",
        state = parcel.readString() ?: "No State",
        country = parcel.readString() ?: "No Country",
        isCity = parcel.readBoolean(),
        latitude = parcel.readDouble(),
        longitude = parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(cityId)
        parcel.writeString(cityName)
        parcel.writeString(county)
        parcel.writeString(state)
        parcel.writeString(country)
        isCity.let { parcel.writeBoolean(it) }
        latitude.let { parcel.writeDouble(it) }
        longitude.let { parcel.writeDouble(it) }
    }

    override fun describeContents() = 0

}

data class CurrentWeatherDomainModel(
    val cityId: Int,
    val temp: Int,
    val detail: String,
    val date: Long,
)


data class DailyForecastDomainModel(
    val cityId: Int,
    val date: Long,
    val minTemp: Int,
    val maxTemp: Int,
    val icon: String,
    val detail: String,
)

data class HourlyForecastDomainModel(
    val cityId: Int,
    val date: Long,
    val temp: Int,
    val icon: String,
)


// Utils
fun CityDomainModel.toLocalModel() =
    CityLocalModel(
        cityId = this.cityId,
        cityName = this.cityName,
        county = this.county,
        state = this.state,
        country = this.country,
        latitude = this.latitude,
        longitude = this.longitude
    )

