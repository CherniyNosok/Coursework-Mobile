package ru.kalinin.coursework_mobile.data.repository

import android.util.Log
import ru.kalinin.coursework_mobile.data.api.CityApiService
import ru.kalinin.coursework_mobile.data.api.WeatherApiService
import ru.kalinin.coursework_mobile.data.model.CityDto
import ru.kalinin.coursework_mobile.data.model.WeatherDto

class WeatherRepository(
    private val cityApi: CityApiService,
    private val weatherApi: WeatherApiService
) {
    suspend fun fetchWeather(cityName: String): Result<Pair<CityDto, WeatherDto>> {
        return try {
            val cities = cityApi.getCityCoordinates(cityName)

            if (cities.isEmpty()) {
                return Result.failure(Exception("City not found"))
            }

            val city = cities.first()
            val weather = weatherApi.getWeather(city.latitude, city.longitude)

            Result.success(city to weather)
        } catch (e: Exception) {
            e.message?.let { Log.e("WEATHER_DEBUG", it) }
            e.printStackTrace()
            Result.failure(e)
        }
    }
}