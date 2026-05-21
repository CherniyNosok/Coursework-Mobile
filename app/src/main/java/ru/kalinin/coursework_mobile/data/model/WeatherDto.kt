package ru.kalinin.coursework_mobile.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDto(
    @SerialName("current") val current: CurrentData,
    @SerialName("hourly") val hourly: HourlyData,
    @SerialName("daily") val daily: DailyData
)