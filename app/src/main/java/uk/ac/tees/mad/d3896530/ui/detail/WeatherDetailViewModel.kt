package uk.ac.tees.mad.d3896530.ui.detail

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3896530.data.repository.WeatherRepository
import uk.ac.tees.mad.d3896530.models.WeatherResponse
import javax.inject.Inject

@HiltViewModel
class WeatherDetailViewModel @Inject constructor(
    private val repository: WeatherRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val lat: Double = checkNotNull(savedStateHandle["lat"])
    val lon: Double = checkNotNull(savedStateHandle["lon"])

    private val _weatherResponse = MutableStateFlow(WeatherResponse())
    val weatherResponse = _weatherResponse.asStateFlow()

    val isLoading = mutableStateOf(true)

    init {
        println("WEATHER DETAIL:::  $lat, $lon")
        fetchWeather(lat, lon, onFailure = {
            Log.e("API ERROR", "Failed to fetch weather")
        })
    }

    private fun fetchWeather(latitude: Double, longitude: Double, onFailure: () -> Unit) =
        viewModelScope.launch {
            val response = repository.getWeatherResponse(latitude, longitude)
            if (response.isSuccessful) {
                response.body()?.let { res ->
                    _weatherResponse.value = res
                }
                isLoading.value = false
            } else {
                onFailure()
            }
        }

}