package ru.kalinin.coursework_mobile.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.kalinin.coursework_mobile.data.model.HourlyEntry
import ru.kalinin.coursework_mobile.utils.toWeatherEmoji

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