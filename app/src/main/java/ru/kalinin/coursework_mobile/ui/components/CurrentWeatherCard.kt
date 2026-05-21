package ru.kalinin.coursework_mobile.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.kalinin.coursework_mobile.R
import ru.kalinin.coursework_mobile.ui.viewmodel.WeatherUiState
import ru.kalinin.coursework_mobile.utils.getWindDirection

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