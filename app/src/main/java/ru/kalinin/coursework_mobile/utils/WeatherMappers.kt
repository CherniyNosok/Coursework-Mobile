package ru.kalinin.coursework_mobile.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.kalinin.coursework_mobile.R

@Composable
fun getWindDirection(degrees: Int): String {
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