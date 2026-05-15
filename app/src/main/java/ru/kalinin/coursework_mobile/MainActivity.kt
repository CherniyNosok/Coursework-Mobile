package ru.kalinin.coursework_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.setValue
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = WeatherRepository(
            cityApi = RetrofitClient.cityService,
            weatherApi = RetrofitClient.weatherService
        )

        val viewModelFactory = WeatherViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[WeatherViewModel::class.java]

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val uiState by viewModel.uiState.collectAsState()

//    var searchQuery by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf<CityConfig?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.weather_forecast_string)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

//            OutlinedTextField(
//                value = searchQuery,
//                onValueChange = { searchQuery = it },
//                label = { Text("Поиск города") },
//                modifier = Modifier.fillMaxWidth(),
//                singleLine = true,
//                trailingIcon = {
//                    IconButton(onClick = { viewModel.fetchWeather(searchQuery) }) {
//                        Icon(imageVector = Icons.Default.Search, contentDescription = "Поиск")
//                    }
//                }
//            )

            Text(
                stringResource(R.string.select_a_city_string),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.labelLarge
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(predefinedCities) { city ->
                    FilterChip(
                        selected = selectedCity == city,
                        onClick = {
                            selectedCity = city
                            viewModel.fetchWeather(city)
                        },
                        label = { Text(stringResource(city.displayName)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (val state = uiState) {
                is WeatherUiState.Empty -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(stringResource(R.string.select_city_string))
                    }
                }
                is WeatherUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is WeatherUiState.Success -> {
                    WeatherDashboard(state = state)
                }
                is WeatherUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherDashboard(state: WeatherUiState.Success) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            CurrentWeatherCard(state)
        }

        item {
            Text(stringResource(R.string.next_24_hours_string), style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.hourlyForecast) { hour ->
                    HourlyItem(hour)
                }
            }
        }

        item {
            Text(stringResource(R.string.forecast_string), style = MaterialTheme.typography.titleLarge)
        }

        items(state.dailyForecast) { day ->
            DailyItem(day)
        }
    }
}

@Composable
fun CurrentWeatherCard(state: WeatherUiState.Success) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(state.city), style = MaterialTheme.typography.headlineSmall)
            Text(
                "${state.currentTemp}°C",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold
            )
            Text("${stringResource(R.string.wind_string)}: ${state.windSpeed} ${stringResource(R.string.speed_string)} (${getWindDirection(state.windDir)})")
        }
    }
}

@Composable
fun HourlyItem(hour: HourlyEntry) {
    Card(
        modifier = Modifier.width(80.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(hour.time.substringAfter("T"), style = MaterialTheme.typography.bodySmall)
            Text("${hour.temp}°C", fontWeight = FontWeight.Bold)
            Text(hour.code.toWeatherEmoji(), style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun DailyItem(day: DailyEntry) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(formatDisplayDate(day.date), modifier = Modifier.weight(1f))
            Text("❄️ ${day.minTemp}°C", color = Color.Blue)
            Spacer(Modifier.width(8.dp))
            Text("🔥 ${day.maxTemp}°C", color = Color.Red)
        }
    }
}

