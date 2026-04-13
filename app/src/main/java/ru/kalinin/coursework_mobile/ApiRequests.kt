package ru.kalinin.coursework_mobile

import retrofit2.http.*

interface CityApiService {
    @GET("city")
    suspend fun getCityCoordinates(
        @Query("name") cityName: String,
        @Header("X-Api-Key") apiKey: String = BuildConfig.X_API_KEY
    ): List<CityDto>
}

interface WeatherApiService {
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("hourly") hourly: String = "temperature_2m",
        @Query("forecast_days") days: Int = 16
    ): WeatherDto
}

