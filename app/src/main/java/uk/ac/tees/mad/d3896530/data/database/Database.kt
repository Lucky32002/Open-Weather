package uk.ac.tees.mad.d3896530.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import uk.ac.tees.mad.d3896530.models.LocationEntity

@Database(entities = [LocationEntity::class], version = 1, exportSchema = false)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}
