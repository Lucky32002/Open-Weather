package uk.ac.tees.mad.d3896530.ui.home

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
class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {


    private val _weatherResponse = MutableStateFlow<WeatherResponse?>(null)
    val weatherResponse = _weatherResponse.asStateFlow()

    fun fetchWeather(
        latitude: Double,
        longitude: Double,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) =
        viewModelScope.launch {
            val response = repository.getWeatherResponse(latitude, longitude)
            if (response.isSuccessful) {
                response.body()?.let { res ->
                    _weatherResponse.value = res
                }
                onSuccess()
            } else {
                onFailure()
            }
        }


}