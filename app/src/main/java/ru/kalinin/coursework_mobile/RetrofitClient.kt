package ru.kalinin.coursework_mobile

import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

private val json = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}

object RetrofitClient {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC // BASIC покажет только URL и Код (200, 401 и т.д.)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    // Клиент для поиска городов (Api-Ninjas)
    private val cityRetrofit = Retrofit.Builder()
        .baseUrl("https://api.api-ninjas.com/v1/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(okHttpClient)
        .build()

    // Клиент для погоды (Open-Meteo)
    private val weatherRetrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(okHttpClient)
        .build()

    val cityService: CityApiService = cityRetrofit.create(CityApiService::class.java)
    val weatherService: WeatherApiService = weatherRetrofit.create(WeatherApiService::class.java)
}

//suspend fun fetchWeather(cityName: String): Result<Pair<CityDto, WeatherDto>> {
//    return try {
//        val cities = RetrofitClient.cityService.getCityCoordinates(cityName, BuildConfig.X_API_KEY)
//
//        if (cities.isEmpty()) {
//            return Result.failure(Exception("Город не найден"))
//        }
//
//        val city = cities.first()
//        val weather = RetrofitClient.weatherService.getWeather(city.latitude, city.longitude)
//
//        Result.success(city to weather)
//    } catch (e: Exception) {
//        // Здесь можно добавить логику: например, проверку на IOException (нет интернета)
//        Result.failure(e)
//    }
//}