package ru.kalinin.coursework_mobile.data.model

data class DailyEntry(
    val date: String,
    val maxTemp: Double,
    val minTemp: Double,
    val code: Int
)