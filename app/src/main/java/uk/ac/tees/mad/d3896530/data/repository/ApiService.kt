package uk.ac.tees.mad.d3896530.data.repository

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uk.ac.tees.mad.d3896530.models.LocationResponse
import uk.ac.tees.mad.d3896530.models.WeatherResponse

interface ApiService {

    @GET("data/2.5/weather")
    suspend fun getWeatherData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String = "b6fcf23b6203ab5fee12d389f6e36df4",
        @Query("units") units: String = "metric"
    ): Response<WeatherResponse>

    @GET("geo/1.0/direct")
    suspend fun searchLocation(
        @Query("q") query: String,
        @Query("limit") limit: Int = 5,
        @Query("appid") apiKey: String = "b6fcf23b6203ab5fee12d389f6e36df4"
    ): Response<List<LocationResponse>>
}
