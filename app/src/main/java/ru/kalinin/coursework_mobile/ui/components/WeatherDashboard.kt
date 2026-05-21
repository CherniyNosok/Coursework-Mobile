package ru.kalinin.coursework_mobile.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.kalinin.coursework_mobile.R
import ru.kalinin.coursework_mobile.ui.viewmodel.WeatherUiState

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