package uk.ac.tees.mad.d3896530.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3896530.data.repository.WeatherRepository
import uk.ac.tees.mad.d3896530.models.LocationEntity
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _favoriteLocations = MutableStateFlow(emptyList<LocationEntity>())
    val favoriteLocations = _favoriteLocations.asStateFlow()


    init {
        fetchFavoriteLocations()
    }

    private fun fetchFavoriteLocations() = viewModelScope.launch {
        repository.getAllLocations().collect {
            _favoriteLocations.value = it
        }
    }

    fun deleteLocation(locationEntity: LocationEntity) = viewModelScope.launch {
        repository.delete(locationEntity)
    }

}
