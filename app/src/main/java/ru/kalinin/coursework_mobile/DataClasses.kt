package ru.kalinin.coursework_mobile

import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CityDto(
    @SerialName("name") val name: String,
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double,
    @SerialName("country") val country: String
)

@Serializable
data class WeatherDto(
    @SerialName("current") val current: CurrentData,
    @SerialName("hourly") val hourly: HourlyData,
    @SerialName("daily") val daily: DailyData
)

@Serializable
data class HourlyData(
    val time: List<String>,
    @SerialName("temperature_2m") val temperatures: List<Double>,
    @SerialName("weather_code") val weatherCodes: List<Int>,
    @SerialName("wind_speed_10m") val windSpeed: List<Double>,
    @SerialName("wind_direction_10m") val windDirection: List<Int>
)

@Serializable
data class CurrentData(
    val time: String,
    @SerialName("temperature_2m") val temperature: Double,
    @SerialName("weather_code") val weatherCode: Int,
    @SerialName("wind_speed_10m") val windSpeed: Double,
    @SerialName("wind_direction_10m") val windDirection: Int
)

@Serializable
data class DailyData(
    val time: List<String>,
    @SerialName("weather_code") val weatherCodes: List<Int>,
    @SerialName("temperature_2m_max") val maxTemps: List<Double>,
    @SerialName("temperature_2m_min") val minTemps: List<Double>,
    @SerialName("wind_speed_10m_max") val maxWindSpeeds: List<Double>,
    @SerialName("wind_direction_10m_dominant") val dominantWindDirections: List<Int>
)

data class HourlyEntry(
    val time: String,
    val temp: Double,
    val code: Int
)
data class DailyEntry(
    val date: String,
    val maxTemp: Double,
    val minTemp: Double,
    val code: Int
)
data class CityConfig(
    @StringRes val displayName: Int,
    val queryName: String
)
val predefinedCities = listOf(
    CityConfig(R.string.city_moscow, "Moscow"),
    CityConfig(R.string.city_saint_petersburg, "Saint Petersburg"),
    CityConfig(R.string.city_novosibirsk, "Novosibirsk"),
    CityConfig(R.string.city_yekaterinburg, "Yekaterinburg"),
    CityConfig(R.string.city_vladivostok, "Vladivostok")
)