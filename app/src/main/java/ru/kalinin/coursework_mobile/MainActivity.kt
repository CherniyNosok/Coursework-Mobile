package ru.kalinin.coursework_mobile

import ads_mobile_sdk.h2
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kalinin.coursework_mobile.ui.theme.CourseworkMobileTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // В реальном проекте это делается через Dependency Injection
        val repository = WeatherRepository(RetrofitClient.cityService, RetrofitClient.weatherService)
        val viewModelFactory = WeatherViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[WeatherViewModel::class.java]

        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    WeatherScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    // Собираем состояние из StateFlow
    val uiState by viewModel.uiState.collectAsState()
    var cityInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Поле ввода и кнопка
        OutlinedTextField(
            value = cityInput,
            onValueChange = { cityInput = it },
            label = { Text("Введите город") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { viewModel.fetchWeather(cityInput) },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Узнать погоду")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Обработка состояний
        when (val state = uiState) {
            is WeatherUiState.Empty -> {
                Text("Введите название города, чтобы начать")
            }
            is WeatherUiState.Loading -> {
                CircularProgressIndicator()
            }
            is WeatherUiState.Success -> {
                WeatherDisplay(state)
            }
            is WeatherUiState.Error -> {
                Text(text = state.message, color = Color.Red)
            }
        }
    }
}

@Composable
fun WeatherDisplay(state: WeatherUiState.Success) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = state.city, style = MaterialTheme.typography.headlineLarge)
        Text(
            text = "${state.temperature}°C",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Прогноз на ближайшее время:", style = MaterialTheme.typography.headlineSmall)

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(state.forecast) { (time, temp) ->
                ForecastItem(time, temp)
            }
        }
    }
}

@Composable
fun ForecastItem(time: String, temp: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Форматируем строку времени (убираем лишние секунды/даты для красоты)
            Text(text = time.substringAfter("T"))
            Text(text = "$temp°C", fontWeight = FontWeight.Bold)
        }
    }
}

sealed class WeatherUiState {
    object Empty : WeatherUiState() // Начальное состояние
    object Loading : WeatherUiState() // Процесс загрузки
    data class Success(
        val city: String,
        val temperature: Double,
        val forecast: List<Pair<String, Double>> // Время и температура
    ) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    // Внутреннее состояние (мутабельное)
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Empty)

    // Внешнее состояние для UI (только для чтения)
    val uiState: StateFlow<WeatherUiState> = _uiState

    fun fetchWeather(cityName: String) {
        if (cityName.isBlank()) {
            _uiState.value = WeatherUiState.Error("Введите название города")
            return
        }

        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            val result = repository.fetchWeather(cityName)

            result.onSuccess { (cityDto, weatherDto) ->
                // Маппим данные из DTO в удобный для UI формат
                // Возьмем, например, первый элемент из списка температур как текущую
                val currentTemp = weatherDto.hourly.temperatures.firstOrNull() ?: 0.0

                // Создаем список пар (Время - Температура)
                val forecastData = weatherDto.hourly.time.zip(weatherDto.hourly.temperatures)

                _uiState.value = WeatherUiState.Success(
                    city = "${cityDto.name}, ${cityDto.country}",
                    temperature = currentTemp,
                    forecast = forecastData
                )
            }.onFailure { exception ->
                _uiState.value = WeatherUiState.Error(
                    exception.message ?: "Произошла неизвестная ошибка"
                )
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