package uk.ac.tees.mad.d3896530.ui.home

import android.Manifest
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import uk.ac.tees.mad.d3896530.BottomNavigationBar
import uk.ac.tees.mad.d3896530.LocationRepository
import uk.ac.tees.mad.d3896530.LocationViewModel
import uk.ac.tees.mad.d3896530.NavDestinations
import uk.ac.tees.mad.d3896530.R
import uk.ac.tees.mad.d3896530.models.Coord
import uk.ac.tees.mad.d3896530.models.WeatherResponse

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val locationViewModel: LocationViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val weather by homeViewModel.weatherResponse.collectAsState()

    val context = LocalContext.current

    val locationRepository = LocationRepository(context, context as ComponentActivity)
    val locationPermissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    val isGpsEnabled = locationRepository.gpsStatus.collectAsState(initial = false)

    val locationState = locationViewModel.locationState

    var isLoading by remember { mutableStateOf(false) }
    val locationPermissionsState = rememberMultiplePermissionsState(
        locationPermissions
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        ) {
            if (!isGpsEnabled.value) {
                Log.d("LOCATION check gps", "Check Gps")
                locationRepository.checkGpsSettings()
            } else {
                Log.d("LOCATION FETCH", "Fetching location updates")
                locationViewModel.initiateLocationUpdates()
            }
        }
    }

    LaunchedEffect(locationPermissionsState.allPermissionsGranted, isGpsEnabled.value) {
        if (locationPermissionsState.allPermissionsGranted) {
            if (!isGpsEnabled.value) {
                locationRepository.checkGpsSettings()
            } else {
                locationViewModel.initiateLocationUpdates()
            }
        } else {
            locationPermissionsState.launchMultiplePermissionRequest()
        }
    }

    LaunchedEffect(locationState.value) {
        Log.d("LOCATION STATE", locationState.value.toString())
        if (locationState.value.lat != 0.0 && locationState.value.lon != 0.0) {
            fetchWeatherData(locationState.value, homeViewModel, context) {
                isLoading = it
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Blue,
                        Color.LightGray
                    )
                )
            )
    ) {
        BottomNavigationBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(18.dp)
                    .align(Alignment.Center),
                color = Color.Blue,
                strokeWidth = 2.dp
            )
        } else {
            Column(Modifier.padding(start = 16.dp, end = 16.dp, top = 40.dp, bottom = 16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        if (locationPermissionsState.allPermissionsGranted) {
                            if (!isGpsEnabled.value) {
                                locationRepository.checkGpsSettings()
                            } else {
                                fetchWeatherData(
                                    locationState.value,
                                    homeViewModel,
                                    context
                                ) {
                                    isLoading = it
                                }
                            }
                        } else {
                            locationPermissionsState.launchMultiplePermissionRequest()
                        }
                    }) {
                    Icon(
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = null,
                        tint = Color.White
                    )

                    Text(
                        text = if (weather?.cod == 200) "${weather?.name}, ${weather?.sys?.country}" else "Select location",
                        color = Color.Black,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )

                    Icon(
                        imageVector = Icons.Rounded.Sync,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    if (weather != null) {
                        if (weather?.cod == 200) {
                            Column(
                                Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    WeatherBackground(
                                        weatherCondition = weather?.weather?.get(0)?.main ?: "",
                                        modifier = Modifier
                                            .size(190.dp)
                                    )
                                    Column {

                                        Row {
                                            Text(
                                                text = "${weather?.main?.temp?.toInt()}",
                                                fontSize = 100.sp,
                                                color = Color.White.copy(0.9f),
                                                letterSpacing = (-5).sp
                                            )
                                            Text(
                                                text = "°",
                                                fontSize = 50.sp,
                                                color = Color.White.copy(0.9f)
                                            )
                                        }
                                        Text(
                                            text = "It's ${weather?.weather?.get(0)?.description}",
                                            color = Color.White.copy(0.9f),
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 22.sp
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(30.dp))
                                WeatherGrid(weatherResponse = weather!!)
                            }
                        } else {
                            Text(
                                text = "Failed to fetch weather",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    } else {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(18.dp)
                                .align(Alignment.Center),
                            color = Color.Blue,
                            strokeWidth = 2.dp
                        )
                    }
                }
            }
        }
    }
}

private fun fetchWeatherData(
    location: Coord,
    homeViewModel: HomeViewModel,
    context: Context,
    setLoading: (Boolean) -> Unit
) {
    println("Location: $location")
    setLoading(true)
    if (location.lat != 0.0 && location.lon != 0.0) {
        homeViewModel.fetchWeather(
            location.lat,
            location.lon,
            onFailure = {
                Toast.makeText(context, "Failed to fetch weather", Toast.LENGTH_SHORT).show()
                setLoading(false)
            },
            onSuccess = {
                setLoading(false)
            }
        )
    } else {
        setLoading(false)
    }
}


@Composable
fun WeatherBackground(weatherCondition: String, modifier: Modifier = Modifier) {
    val animationResId = when (weatherCondition) {
        "Clear" -> R.raw.clear_sky
        "Clouds" -> R.raw.cloudy
        "Rain" -> R.raw.rainy
        "Snow" -> R.raw.snowy
        "Thunderstorm" -> R.raw.thunderstorm
        "Haze" -> R.raw.hazy
        "Mist" -> R.raw.mist
        else -> R.raw.default_weather
    }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animationResId))

    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier
    )

}


@Composable
fun WeatherGrid(weatherResponse: WeatherResponse) {
    Column {
        WeatherDetail(
            icon = Icons.Default.Thermostat,
            label = "Feels Like",
            value = "${weatherResponse.main.feels_like}°C"
        )
        WeatherDetail(
            icon = Icons.Default.Speed,
            label = "Pressure",
            value = "${weatherResponse.main.pressure} hPa"
        )
        WeatherDetail(
            icon = Icons.Default.WaterDrop,
            label = "Humidity",
            value = "${weatherResponse.main.humidity}%"
        )


        WeatherDetail(
            icon = Icons.Default.Visibility,
            label = "Visibility",
            value = "${weatherResponse.visibility} meters"
        )


        WeatherDetail(
            icon = Icons.Default.Air,
            label = "Wind Speed",
            value = "${weatherResponse.wind.speed} m/s"
        )
    }
}

@Composable
fun WeatherDetail(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "$label Icon",
            modifier = Modifier
                .size(40.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )
    }
}


object HomeDes : NavDestinations {
    override val navRoute = "home"
}