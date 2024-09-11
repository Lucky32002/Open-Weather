package uk.ac.tees.mad.d3896530.data.repository

import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import uk.ac.tees.mad.d3896530.data.database.LocationDao
import uk.ac.tees.mad.d3896530.models.LocationEntity
import uk.ac.tees.mad.d3896530.models.WeatherResponse
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val apiService: ApiService,
    private val locationDao: LocationDao
) {
    suspend fun getWeatherResponse(
        latitude: Double,
        longitude: Double
    ): Response<WeatherResponse> =
        apiService.getWeatherData(latitude, longitude)

    suspend fun insert(location: LocationEntity) = locationDao.insert(location)

    fun getAllLocations(): Flow<List<LocationEntity>> = locationDao.getAllLocations()

    suspend fun delete(location: LocationEntity) = locationDao.delete(location)
}