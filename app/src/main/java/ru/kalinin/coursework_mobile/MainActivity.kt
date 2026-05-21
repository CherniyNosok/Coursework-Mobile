package ru.kalinin.coursework_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import ru.kalinin.coursework_mobile.data.network.RetrofitClient
import ru.kalinin.coursework_mobile.data.repository.WeatherRepository
import ru.kalinin.coursework_mobile.ui.WeatherScreen
import ru.kalinin.coursework_mobile.ui.viewmodel.WeatherViewModel
import ru.kalinin.coursework_mobile.ui.viewmodel.WeatherViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = WeatherRepository(
            cityApi = RetrofitClient.cityService,
            weatherApi = RetrofitClient.weatherService
        )

        val viewModelFactory = WeatherViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[WeatherViewModel::class.java]

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherScreen(viewModel = viewModel)
                }
            }
        }
    }
}


