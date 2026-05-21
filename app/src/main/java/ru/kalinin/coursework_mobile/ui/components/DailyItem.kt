package ru.kalinin.coursework_mobile.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.kalinin.coursework_mobile.data.model.DailyEntry
import ru.kalinin.coursework_mobile.utils.formatDisplayDate

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