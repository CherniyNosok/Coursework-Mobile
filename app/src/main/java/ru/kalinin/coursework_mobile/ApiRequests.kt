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
        @Query("daily") daily: String = "weather_code,temperature_2m_max,temperature_2m_min,wind_speed_10m_max,wind_direction_10m_dominant",
        @Query("hourly") hourly: String = "temperature_2m,weather_code,wind_speed_10m,wind_direction_10m",
        @Query("current") current: String = "temperature_2m,weather_code,wind_speed_10m,wind_direction_10m",
        @Query("past_days") pastDays: Int = 1,
        @Query("forecast_days") days: Int = 3,
        @Query("timezone") timezone: String = "auto",
    ): WeatherDto
}

