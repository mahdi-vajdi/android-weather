package com.mahdivajdi.modernweather.data.remote

import com.mahdivajdi.modernweather.data.local.CurrentWeatherLocalModel
import com.mahdivajdi.modernweather.data.local.DailyForecastLocalModel
import com.mahdivajdi.modernweather.data.local.HourlyForecastLocalModel
import com.squareup.moshi.Json


data class OneCallApiResponseModel(
    @Json(name = "timezone")
    val timezone: String = "",
    @Json(name = "lon")
    val lon: Double = 0.0,
    @Json(name = "lat")
    val lat: Double = 0.0,
    @Json(name = "timezone_offset")
    val timezoneOffset: Int = 0,
    @Json(name = "current")
    val currentWeatherRemoteModel: CurrentWeatherRemoteModel,
    @Json(name = "hourly")
    val hourly: List<HourlyForecastRemoteModel>?,
    @Json(name = "daily")
    val daily: List<DailyForecastRemoteModel>?,
)


data class CurrentWeatherRemoteModel(
    @Json(name = "sunrise")
    val sunrise: Int = 0,
    @Json(name = "temp")
    val temp: Double = 0.0,
    @Json(name = "visibility")
    val visibility: Int = 0,
    @Json(name = "uvi")
    val uvi: Double = 0.0,
    @Json(name = "pressure")
    val pressure: Int = 0,
    @Json(name = "clouds")
    val clouds: Int = 0,
    @Json(name = "feels_like")
    val feelsLike: Double = 0.0,
    @Json(name = "dt")
    val dt: Long = 0,
    @Json(name = "wind_deg")
    val windDeg: Int = 0,
    @Json(name = "dew_point")
    val dewPoint: Double = 0.0,
    @Json(name = "sunset")
    val sunset: Int = 0,
    @Json(name = "weather")
    val weather: List<WeatherItem>?,
    @Json(name = "humidity")
    val humidity: Int = 0,
    @Json(name = "wind_speed")
    val windSpeed: Double = 0.0,
)


data class HourlyForecastRemoteModel(
    @Json(name = "temp")
    val temp: Double = 0.0,
    @Json(name = "visibility")
    val visibility: Int = 0,
    @Json(name = "uvi")
    val uvi: Double = 0.0,
    @Json(name = "pressure")
    val pressure: Int = 0,
    @Json(name = "clouds")
    val clouds: Int = 0,
    @Json(name = "feels_like")
    val feelsLike: Double = 0.0,
    @Json(name = "wind_gust")
    val windGust: Double = 0.0,
    @Json(name = "dt")
    val dt: Long = 0,
    @Json(name = "pop")
    val pop: Double = 0.0,
    @Json(name = "wind_deg")
    val windDeg: Int = 0,
    @Json(name = "dew_point")
    val dewPoint: Double = 0.0,
    @Json(name = "weather")
    val weather: List<WeatherItem>?,
    @Json(name = "humidity")
    val humidity: Int = 0,
    @Json(name = "wind_speed")
    val windSpeed: Double = 0.0,
)


data class DailyForecastRemoteModel(
    @Json(name = "moonset")
    val moonset: Int = 0,
    @Json(name = "rain")
    val rain: Double = 0.0,
    @Json(name = "sunrise")
    val sunrise: Int = 0,
    @Json(name = "temp")
    val temp: Temp,
    @Json(name = "moon_phase")
    val moonPhase: Double = 0.0,
    @Json(name = "uvi")
    val uvi: Double = 0.0,
    @Json(name = "moonrise")
    val moonrise: Int = 0,
    @Json(name = "pressure")
    val pressure: Int = 0,
    @Json(name = "clouds")
    val clouds: Int = 0,
    @Json(name = "feels_like")
    val feelsLike: FeelsLike,
    @Json(name = "wind_gust")
    val windGust: Double = 0.0,
    @Json(name = "dt")
    val dt: Long = 0,
    @Json(name = "pop")
    val pop: Double = 0.0,
    @Json(name = "wind_deg")
    val windDeg: Int = 0,
    @Json(name = "dew_point")
    val dewPoint: Double = 0.0,
    @Json(name = "sunset")
    val sunset: Int = 0,
    @Json(name = "weather")
    val weather: List<WeatherItem>?,
    @Json(name = "humidity")
    val humidity: Int = 0,
    @Json(name = "wind_speed")
    val windSpeed: Double = 0.0,
)


data class FeelsLike(
    @Json(name = "eve")
    val eve: Double = 0.0,
    @Json(name = "night")
    val night: Double = 0.0,
    @Json(name = "day")
    val day: Double = 0.0,
    @Json(name = "morn")
    val morn: Double = 0.0,
)


data class Temp(
    @Json(name = "min")
    val min: Double = 0.0,
    @Json(name = "max")
    val max: Double = 0.0,
    @Json(name = "eve")
    val eve: Double = 0.0,
    @Json(name = "night")
    val night: Double = 0.0,
    @Json(name = "day")
    val day: Double = 0.0,
    @Json(name = "morn")
    val morn: Double = 0.0,
)


data class WeatherItem(
    @Json(name = "icon")
    val icon: String = "",
    @Json(name = "description")
    val description: String = "",
    @Json(name = "main")
    val main: String = "",
    @Json(name = "id")
    val id: Int = 0,
)


// transfer objects
fun CurrentWeatherRemoteModel.asCurrentWeatherLocalModel(cityId: Int) =
    CurrentWeatherLocalModel(
        cityId = cityId,
        temp = this.temp,
        detail = this.weather?.get(0)?.description ?: "",
        date = this.dt
    )

fun HourlyForecastRemoteModel.asHourlyWeatherLocalModel(cityId: Int) =
    HourlyForecastLocalModel(
        cityId = cityId,
        date = this.dt,
        temp = this.temp,
        icon = this.weather!![0].icon
    )

fun DailyForecastRemoteModel.asDailyForecastLocalModel(cityId: Int) =
    DailyForecastLocalModel(
        cityId = cityId,
        date = this.dt,
        minTemp = this.temp.min,
        maxTemp = this.temp.max,
        icon = this.weather!![0].icon,
        detail = this.weather[0].main
    )