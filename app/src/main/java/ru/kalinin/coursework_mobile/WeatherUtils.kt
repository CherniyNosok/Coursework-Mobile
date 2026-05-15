package ru.kalinin.coursework_mobile

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun formatDisplayDate(dateString: String): String {
    val date = LocalDate.parse(dateString)
    val now = LocalDate.now()
    return when (date) {
        now.minusDays(1) -> stringResource(R.string.day_yesterday)
        now -> stringResource(R.string.day_today)
        now.plusDays(1) -> stringResource(R.string.day_tomorrow)
        else -> date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }
}

@Composable
fun getWindDirection(degrees: Int): String {
    //val directions = listOf("С", "СВ", "В", "ЮВ", "Ю", "ЮЗ", "З", "СЗ")
    val directions = listOf(
        stringResource(R.string.direction_north),
        stringResource(R.string.direction_northeast),
        stringResource(R.string.direction_east),
        stringResource(R.string.direction_southeast),
        stringResource(R.string.direction_south),
        stringResource(R.string.direction_southwest),
        stringResource(R.string.direction_west),
        stringResource(R.string.direction_northwest)
    )
    val index = ((degrees + 22.5) / 45).toInt() % 8
    return directions[index]
}

fun Int.toWeatherEmoji(): String {
    return when (this) {
        0 -> "☀️"
        1, 2, 3 -> "🌤️"
        45, 48 -> "🌫️"
        51, 53, 55 -> "🌦️"
        61, 63, 65 -> "🌧️"
        71, 73, 75 -> "❄️"
        95, 96, 99 -> "⛈️"
        else -> "☁️"
    }
}