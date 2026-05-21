package ru.kalinin.coursework_mobile.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyData(
    val time: List<String>,
    @SerialName("weather_code") val weatherCodes: List<Int>,
    @SerialName("temperature_2m_max") val maxTemps: List<Double>,
    @SerialName("temperature_2m_min") val minTemps: List<Double>,
    @SerialName("wind_speed_10m_max") val maxWindSpeeds: List<Double>,
    @SerialName("wind_direction_10m_dominant") val dominantWindDirections: List<Int>
)