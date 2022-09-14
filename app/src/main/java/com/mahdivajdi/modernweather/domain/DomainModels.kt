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

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "No City",
        parcel.readString() ?: "No County",
        parcel.readString() ?: "No State",
        parcel.readString() ?: "No Country",
        parcel.readByte() != 0.toByte(),
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(cityId)
        parcel.writeString(cityName)
        parcel.writeString(county)
        parcel.writeString(state)
        parcel.writeString(country)
        parcel.writeByte(if (isCity) 1 else 0)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CityDomainModel> {
        override fun createFromParcel(parcel: Parcel): CityDomainModel {
            return CityDomainModel(parcel)
        }

        override fun newArray(size: Int): Array<CityDomainModel?> {
            return arrayOfNulls(size)
        }
    }
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

