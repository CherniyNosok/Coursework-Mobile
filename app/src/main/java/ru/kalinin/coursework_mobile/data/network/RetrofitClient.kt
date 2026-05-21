package ru.kalinin.coursework_mobile.data.network

import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.kalinin.coursework_mobile.data.api.CityApiService
import ru.kalinin.coursework_mobile.data.api.WeatherApiService

private val json = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}

object RetrofitClient {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    private val cityRetrofit = Retrofit.Builder()
        .baseUrl("https://api.api-ninjas.com/v1/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(okHttpClient)
        .build()
    private val weatherRetrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(okHttpClient)
        .build()

    val cityService: CityApiService = cityRetrofit.create(CityApiService::class.java)
    val weatherService: WeatherApiService = weatherRetrofit.create(WeatherApiService::class.java)
}