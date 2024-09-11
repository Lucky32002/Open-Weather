package uk.ac.tees.mad.d3896530.ui.addlocation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3896530.data.repository.ApiService
import uk.ac.tees.mad.d3896530.data.repository.WeatherRepository
import uk.ac.tees.mad.d3896530.models.LocationEntity
import uk.ac.tees.mad.d3896530.models.LocationResponse
import javax.inject.Inject

@HiltViewModel
class AddLocationViewModel @Inject constructor(
    private val apiService: ApiService,
    private val repository: WeatherRepository
) : ViewModel() {
    private val _searchResults = MutableStateFlow<List<LocationResponse>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    fun searchLocation(query: String) {
        viewModelScope.launch {
            try {
                val response = apiService.searchLocation(query)
                Log.d("AddLocationViewModel", "Search results: ${response}")
                if (response.isSuccessful) {
                    _searchResults.value = response.body()!!
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addLocation(location: LocationResponse) {
        viewModelScope.launch {
            repository.insert(LocationEntity.fromResponse(location))
        }.invokeOnCompletion {
            Log.d("AddLocationViewModel", "Location added")
        }
    }
}
