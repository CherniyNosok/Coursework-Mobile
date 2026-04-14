package ru.kalinin.coursework_mobile

import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

fun formatDisplayDate(dateString: String): String {
    val date = LocalDate.parse(dateString)
    val now = LocalDate.now()
    return when (date) {
        now.minusDays(1) -> "Вчера"
        now -> "Сегодня"
        now.plusDays(1) -> "Завтра"
        else -> date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("ru"))
    }
}

fun getWindDirection(degrees: Int): String {
    val directions = listOf("С", "СВ", "В", "ЮВ", "Ю", "ЮЗ", "З", "СЗ")
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