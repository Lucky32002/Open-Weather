package uk.ac.tees.mad.d3896530.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.ac.tees.mad.d3896530.NavDestinations
import uk.ac.tees.mad.d3896530.ui.home.WeatherBackground
import uk.ac.tees.mad.d3896530.ui.home.WeatherGrid

@Composable
fun WeatherDetailScreen(
    onBack: () -> Unit,
    weatherDetailViewModel: WeatherDetailViewModel = hiltViewModel()
) {
    val weather by weatherDetailViewModel.weatherResponse.collectAsState()
    val isLoading by weatherDetailViewModel.isLoading

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
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center),
                color = Color.Blue,
                strokeWidth = 2.dp
            )
        }
        Column(Modifier.padding(start = 16.dp, end = 16.dp, top = 40.dp, bottom = 16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBackIosNew,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = if (weather.cod == 200) "${weather.name}, ${weather.sys.country}" else "Select location",
                    color = Color.Black,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )

                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (weather.cod == 200) {
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
                                weatherCondition = weather.weather[0].main,
                                modifier = Modifier
                                    .size(190.dp)
                            )
//                            Column {
//                                val iconUrl =
//                                    "https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png"
//                                Image(
//                                    painter = rememberAsyncImagePainter(iconUrl),
//                                    contentDescription = "Weather Icon",
//                                    modifier = Modifier
//                                        .size(60.dp)
//                                        .scale(2f),
//                                    contentScale = ContentScale.Crop
//                                )
//
//                            }
                            Column {

                                Row {
                                    Text(
                                        text = "${weather.main.temp.toInt()}",
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
                                    text = "It's ${weather.weather[0].description}",
                                    color = Color.White.copy(0.9f),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 22.sp
                                )
                            }
                        }
                        WeatherGrid(weatherResponse = weather)
                    }
                } else {
                    Text(
                        text = "Failed to fetch weather",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

object WeatherDetailNav : NavDestinations {
    override val navRoute = "weather_details"
    const val latArg = "lat"
    const val lonArg = "lon"
    val routeWithArg = "$navRoute/{$latArg}/{$lonArg}"
}