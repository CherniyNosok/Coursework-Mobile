package ru.kalinin.coursework_mobile.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.kalinin.coursework_mobile.R
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