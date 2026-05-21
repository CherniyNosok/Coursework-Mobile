package ru.kalinin.coursework_mobile.ui.viewmodel

import ru.kalinin.coursework_mobile.data.model.DailyEntry
import ru.kalinin.coursework_mobile.data.model.HourlyEntry

sealed class WeatherUiState {
    object Empty : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(
        val city: Int,
        val currentTemp: Double,
        val windSpeed: Double,
        val windDir: Int,
        val weatherCode: Int,
        val hourlyForecast: List<HourlyEntry>,
        val dailyForecast: List<DailyEntry>
    ) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}