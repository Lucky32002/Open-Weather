package uk.ac.tees.mad.d3896530.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey val id: String,
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String
) {
    companion object {
        fun fromResponse(response: LocationResponse): LocationEntity {
            return LocationEntity(
                id = "${response.lat},${response.lon}",
                name = response.name,
                lat = response.lat,
                lon = response.lon,
                country = response.country
            )
        }
    }
}
