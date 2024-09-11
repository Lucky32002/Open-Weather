package uk.ac.tees.mad.d3896530.di

import android.app.Application
import androidx.room.Room
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import uk.ac.tees.mad.d3896530.data.database.LocationDao
import uk.ac.tees.mad.d3896530.data.database.LocationDatabase
import uk.ac.tees.mad.d3896530.data.repository.ApiService
import uk.ac.tees.mad.d3896530.data.repository.AuthRepository
import uk.ac.tees.mad.d3896530.data.repository.WeatherRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore


    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = Firebase.storage


    @Provides
    @Singleton
    fun provideAuthRepo(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ): AuthRepository = AuthRepository(firebaseAuth, firebaseFirestore, firebaseStorage)


    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun providesApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun providesWeatherRepo(apiService: ApiService, locationDao: LocationDao) =
        WeatherRepository(apiService, locationDao)

    @Provides
    @Singleton
    fun providesDatabase(application: Application): LocationDatabase =
        Room.databaseBuilder(
            application,
            LocationDatabase::class.java,
            "app_database"
        ).build()

    @Provides
    @Singleton
    fun providesLocationDao(db: LocationDatabase) = db.locationDao()
}