package ru.kalinin.coursework_mobile.data.model

import androidx.annotation.StringRes
data class CityConfig(
    @StringRes val displayName: Int,
    val queryName: String
)