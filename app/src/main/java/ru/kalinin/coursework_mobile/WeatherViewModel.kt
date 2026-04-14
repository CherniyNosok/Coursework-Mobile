package ru.kalinin.coursework_mobile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class WeatherUiState {
    object Empty : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(
        val city: String,
        val currentTemp: Double,
        val windSpeed: Double,
        val windDir: Int,
        val weatherCode: Int,
        val hourlyForecast: List<HourlyEntry>,
        val dailyForecast: List<DailyEntry>
    ) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Empty)
    val uiState: StateFlow<WeatherUiState> = _uiState

    fun fetchWeather(cityName: String) {
        if (cityName.isBlank()) {
            _uiState.value = WeatherUiState.Error("Введите название города")
            return
        }

        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            repository.fetchWeather(cityName)
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
                        DailyEntry(daily.time[i], daily.maxTemps[i], daily.minTemps[i], daily.weatherCodes[i])
                    }

                    _uiState.value = WeatherUiState.Success(
                        city = "${cityDto.name}, ${cityDto.country}",
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

class WeatherViewModelFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}