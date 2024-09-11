package uk.ac.tees.mad.d3896530.models

data class WeatherResponse(
    val coord: Coord = Coord(0.0, 0.0),
    val weather: List<Weather> = listOf(),
    val base: String = "",
    val main: Main = Main(0.0, 0.0, 0.0, 0.0, 0, 0, 0, 0),
    val visibility: Int = 0,
    val wind: Wind = Wind(0.0, 0, 0.0),
    val clouds: Clouds = Clouds(0),
    val dt: Long = 0,
    val sys: Sys = Sys(0, 0, "", 0, 0),
    val timezone: Int = 0,
    val id: Int = 0,
    val name: String = "",
    val cod: Int = 0
)

data class Coord(
    val lon: Double,
    val lat: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int,
    val grnd_level: Int
)

data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double
)

data class Clouds(
    val all: Int
)

data class Sys(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)