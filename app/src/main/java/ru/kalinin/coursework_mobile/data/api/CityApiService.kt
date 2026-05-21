package ru.kalinin.coursework_mobile.data.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import ru.kalinin.coursework_mobile.BuildConfig
import ru.kalinin.coursework_mobile.data.model.CityDto

interface CityApiService {
    @GET("city")
    suspend fun getCityCoordinates(
        @Query("name") cityName: String,
        @Header("X-Api-Key") apiKey: String = BuildConfig.X_API_KEY
    ): List<CityDto>
}
