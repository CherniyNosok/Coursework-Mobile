package ru.kalinin.coursework_mobile

import android.util.Log

class WeatherRepository(
    private val cityApi: CityApiService,
    private val weatherApi: WeatherApiService
) {
    suspend fun fetchWeather(cityName: String): Result<Pair<CityDto, WeatherDto>> {
        return try {
            val cities = cityApi.getCityCoordinates(cityName)

            if (cities.isEmpty()) {
                return Result.failure(Exception("Город не найден"))
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