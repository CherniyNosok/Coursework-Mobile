package ru.kalinin.coursework_mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kalinin.coursework_mobile.data.model.CityConfig
import ru.kalinin.coursework_mobile.data.model.DailyEntry
import ru.kalinin.coursework_mobile.data.model.HourlyEntry
import ru.kalinin.coursework_mobile.data.repository.WeatherRepository

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Empty)
    val uiState: StateFlow<WeatherUiState> = _uiState

    fun fetchWeather(cityConfig: CityConfig) {

        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            val result = repository.fetchWeather(cityConfig.queryName)
                .onSuccess { (cityDto, weatherDto) ->
                    val current = weatherDto.current
                    val hourly = weatherDto.hourly
                    val daily = weatherDto.daily

                    val startIndex = hourly.time.indexOfFirst { it >= current.time }.coerceAtLeast(0)
                    val endIndex = (startIndex + 24).coerceAtMost(hourly.time.size)

                    val hourlyList = (startIndex until endIndex).map { i ->
                        HourlyEntry(
                            time = hourly.time[i],
                            temp = hourly.temperatures[i],
                            code = hourly.weatherCodes[i]
                        )
                    }

                    val dailyList = daily.time.indices.map { i ->
                        DailyEntry(
                            daily.time[i],
                            daily.maxTemps[i],
                            daily.minTemps[i],
                            daily.weatherCodes[i]
                        )
                    }

                    _uiState.value = WeatherUiState.Success(
                        city = cityConfig.displayName,
                        currentTemp = current.temperature,
                        windSpeed = current.windSpeed,
                        windDir = current.windDirection,
                        weatherCode = current.weatherCode,
                        hourlyForecast = hourlyList,
                        dailyForecast = dailyList
                    )
                }
                .onFailure { exception ->
                    _uiState.value = WeatherUiState.Error(exception.message ?: "Ошибка сети")
                }
        }
    }
}