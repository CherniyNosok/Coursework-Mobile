package ru.kalinin.coursework_mobile.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourlyData(
    val time: List<String>,
    @SerialName("temperature_2m") val temperatures: List<Double>,
    @SerialName("weather_code") val weatherCodes: List<Int>,
    @SerialName("wind_speed_10m") val windSpeed: List<Double>,
    @SerialName("wind_direction_10m") val windDirection: List<Int>
)